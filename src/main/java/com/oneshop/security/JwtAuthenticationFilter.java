package com.oneshop.security;

import com.oneshop.entity.User;
import com.oneshop.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwt;
  private final UserRepository userRepo;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    // Nếu đã có auth thì bỏ qua (tránh set lại)
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      String header = req.getHeader(HttpHeaders.AUTHORIZATION);
      if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);
        try {
          String subject = jwt.getSubject(token); // username hoặc email
          User u = userRepo.findByUsername(subject)
              .orElseGet(() -> userRepo.findByEmail(subject).orElse(null));

          if (u != null && u.isEnabled()) {
            var authorities = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toSet());

            var auth = new UsernamePasswordAuthenticationToken(u.getUsername(), null, authorities);
            // (optional) gắn request details
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(auth);
          }
        } catch (Exception ignored) {}
      }
    }

    chain.doFilter(req, res);
  }
}
