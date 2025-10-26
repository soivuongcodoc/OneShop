package com.oneshop.security;

import com.oneshop.entity.User;
import com.oneshop.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwt;
  private final UserRepository userRepo;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest req,
                                  @NonNull HttpServletResponse res,
                                  @NonNull FilterChain chain)
      throws ServletException, IOException {

    // Nếu đã có auth thì bỏ qua (tránh set lại)
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      String header = req.getHeader(HttpHeaders.AUTHORIZATION);
      String token = null;
      if (header != null && header.startsWith("Bearer ")) {
        token = header.substring(7);
      } else if (req.getCookies() != null) {
        // Fallback: đọc JWT từ cookie tên "JWT" hoặc "jwtToken"
        for (var c : req.getCookies()) {
          if (("JWT".equals(c.getName()) || "jwtToken".equalsIgnoreCase(c.getName()))
              && c.getValue() != null && !c.getValue().isBlank()) {
            token = c.getValue();
            break;
          }
        }
      }

      if (token != null) {
        try {
          if (jwt.validateToken(token)) {
            String username = jwt.getSubject(token); // username hoặc email
            List<String> roleNames = jwt.getRoles(token);

            Set<SimpleGrantedAuthority> authorities;
            if (roleNames != null && !roleNames.isEmpty()) {
              authorities = roleNames.stream()
                  .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                  .map(SimpleGrantedAuthority::new)
                  .collect(Collectors.toSet());
            } else {
              // Fallback: load từ DB nếu token không có roles
              User u = userRepo.findByUsername(username)
                  .orElseGet(() -> userRepo.findByEmail(username).orElse(null));
              if (u == null || !u.isEnabled()) {
                chain.doFilter(req, res);
                return;
              }
              authorities = u.getRoles().stream()
                  .map(r -> new SimpleGrantedAuthority(r.getName()))
                  .collect(Collectors.toSet());
            }

            var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(auth);
          }
        } catch (Exception ignored) {}
      }
    }

    chain.doFilter(req, res);
  }
}
