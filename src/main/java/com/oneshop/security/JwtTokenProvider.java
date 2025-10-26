package com.oneshop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oneshop.entity.Role;
import com.oneshop.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
  private final Key key;
  private final long validityMs;

  public JwtTokenProvider(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration}") long validityMs) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.validityMs = validityMs;
  }

  // Legacy: subject-only token (kept for compatibility in case some clients rely on it)
  public String generateToken(String subject) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validityMs);
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  // New: generate token including roles claim
  public String generateToken(User user) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validityMs);
    List<String> roles = user.getRoles().stream().map(Role::getName).toList();
    return Jwts.builder()
        .setSubject(user.getUsername())
        .claim("roles", roles)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getSubject(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  @SuppressWarnings("unchecked")
  public List<String> getRoles(String token) {
    return (List<String>) Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().get("roles", List.class);
  }

  public boolean validateToken(String token) {
      try {
          Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
          return true;
      } catch (JwtException | IllegalArgumentException e) {
          return false;
      }
  }
}
