
package com.oneshop.controller.auth;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oneshop.dto.auth.AuthDtos.ForgotPasswordRequest;
import com.oneshop.dto.auth.AuthDtos.JwtResponse;
import com.oneshop.dto.auth.AuthDtos.LoginRequest;
import com.oneshop.dto.auth.AuthDtos.RegisterRequest;
import com.oneshop.dto.auth.AuthDtos.ResetPasswordRequest;
import com.oneshop.dto.auth.AuthDtos.VerifyEmailRequest;
import com.oneshop.security.JwtTokenProvider;
import com.oneshop.service.auth.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final JwtTokenProvider jwt;

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
    authService.register(req);
    return ResponseEntity.ok("Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.");
  }

  @PostMapping("/verify")
  public ResponseEntity<?> verify(@Valid @RequestBody VerifyEmailRequest req) {
    authService.verifyEmail(req);
    return ResponseEntity.ok("Xác thực email thành công!");
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest req) {
    return ResponseEntity.ok(authService.login(req));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgot(@Valid @RequestBody ForgotPasswordRequest req) {
    authService.forgotPassword(req);
    return ResponseEntity.ok("OTP đặt lại mật khẩu đã được gửi về email.");
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> reset(@Valid @RequestBody ResetPasswordRequest req) {
    authService.resetPassword(req);
    return ResponseEntity.ok("Đặt lại mật khẩu thành công!");
  }
  @GetMapping("/test")
  public ResponseEntity<String> testAuth() {
      return ResponseEntity.ok("JWT xác thực hợp lệ, chào mừng bạn!");
  }
  @GetMapping("/verify-token")
  public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
      }

      String token = authHeader.substring(7);
      if (!jwt.validateToken(token)) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
      }

      var roles = jwt.getRoles(token);
      var username = jwt.getSubject(token);

      return ResponseEntity.ok(Map.of(
          "username", username,
          "roles", roles
      ));
  }
    // @PostMapping("/logout")
    // public ResponseEntity<?> logout() {
    //     // Clear JWT cookie
    //     ResponseCookie clear = ResponseCookie.from("JWT", "")
    //             .httpOnly(true)
    //             .secure(false)
    //             .path("/")
    //             .maxAge(0)
    //             .sameSite("Lax")
    //             .build();
    //     return ResponseEntity.ok()
    //             .header(HttpHeaders.SET_COOKIE, clear.toString())
    //             .body("Đăng xuất thành công");
    // }
    // // Endpoint test đã bỏ

}
