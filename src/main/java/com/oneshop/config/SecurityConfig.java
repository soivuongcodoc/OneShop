package com.oneshop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.oneshop.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

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
          .cors(cors -> cors.disable())
          // .formLogin(form -> form.disable()) // Không dùng form login
          // .httpBasic(basic -> basic.disable()) // Không dùng HTTP Basic
          .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless
          .authorizeHttpRequests(auth -> auth
          // ✅ Cho phép public các tài nguyên tĩnh và trang auth

                .requestMatchers(
                    "/", "/index",
                    "/css/**", "/js/**", "/images/**",
                    "/decorators/**", "/fragments/**",
                    "/auth/**",
                    "/login", "/register", "/verify", "/verify/**",
                    "/forgot-password", "/reset-password",
                    "/api/auth/**",
                    "/favicon.ico"
                ).permitAll()

              .requestMatchers("/vendor/**").hasAnyRole("VENDOR", "ADMIN")
              .requestMatchers("/admin/**").hasRole("ADMIN")
              .requestMatchers("/home").hasAnyRole("USER", "ADMIN","VENDOR")
              .anyRequest().authenticated()
          ) 
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
  }

}
