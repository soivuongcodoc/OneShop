package com.oneshop.config;

import com.oneshop.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtFilter;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
          .csrf(csrf -> csrf.disable()) // REST API + JWT => disable CSRF
          .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(
            		  "/",
            		  "/home",
                  "/decorators/**",
                  "/css/**",
                  "/js/**",
                  "/images/**",
                  "/login",
                  "/register",
                  "/verify",
                  "/verify/**",
                  "/api/auth/**",      
                  "/forgot-password",
                  "/reset-password"
              ).permitAll()
              .anyRequest().authenticated()
          )
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
  }
}
