package com.oneshop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

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

  public String getSubject(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().getSubject();
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
