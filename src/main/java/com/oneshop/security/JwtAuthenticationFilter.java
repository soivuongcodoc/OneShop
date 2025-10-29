package com.oneshop.security;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oneshop.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component @RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwt;
  private final UserRepository userRepo;

 @Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		String header = req.getHeader(HttpHeaders.AUTHORIZATION);
		String token = null;

		// 🔹 Lấy token từ Header (nếu có)
		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
		}

		// 🔹 Nếu không có trong header thì lấy từ Cookie
		if (token == null && req.getCookies() != null) {
			for (Cookie c : req.getCookies()) {
				if ("jwtToken".equals(c.getName())) {
					token = c.getValue(); // ❌ KHÔNG còn "Bearer " trong cookie nữa
					break;
				}
			}
		}

		// 🔹 Nếu có token -> xác thực
		if (token != null) {
			try {
				if (jwt.validateToken(token)) {
					String username = jwt.getSubject(token);
					List<String> roles = jwt.getRoles(token);

					System.out.println("🔍 Token from cookie/header: " + token);
					System.out.println("Roles from JWT: " + roles);

					Set<SimpleGrantedAuthority> authorities = roles.stream()
							.map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r).map(SimpleGrantedAuthority::new)
							.collect(Collectors.toSet());

					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
							authorities);
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

					SecurityContextHolder.getContext().setAuthentication(auth);
					System.out.println(
							"✅ SecurityContext after set: " + SecurityContextHolder.getContext().getAuthentication());
				} else {
					System.out.println("❌ Token invalid theo validateToken()");
				}
			} catch (Exception e) {
				System.out.println("🔥 JWT parse error: " + e.getMessage());
			}
		} else {
			System.out.println("⚠️ Không có token trong request " + req.getRequestURI());
		}

		chain.doFilter(req, res);
	}
}