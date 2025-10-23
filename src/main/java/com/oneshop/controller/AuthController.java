package com.oneshop.controller;

import com.oneshop.dto.AuthDtos.*;
import com.oneshop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
    authService.register(req);
    return ResponseEntity.ok("Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.");
  }

  @PostMapping("/verify-email")
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

}
