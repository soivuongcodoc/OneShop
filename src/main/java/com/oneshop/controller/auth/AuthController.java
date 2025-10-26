package com.oneshop.controller.auth;

import com.oneshop.dto.auth.AuthDtos.*;
import com.oneshop.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;

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
    JwtResponse jwt = authService.login(req);

    // Set HttpOnly cookie so SSR pages can read JWT securely
    ResponseCookie cookie = ResponseCookie.from("JWT", jwt.getToken())
        .httpOnly(true)
        .secure(false) // set true when using HTTPS
        .path("/")
        .maxAge(24 * 60 * 60) // 1 day
        .sameSite("Lax")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(jwt);
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

  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    // Clear JWT cookie
    ResponseCookie clear = ResponseCookie.from("JWT", "")
        .httpOnly(true)
        .secure(false)
        .path("/")
        .maxAge(0)
        .sameSite("Lax")
        .build();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, clear.toString())
        .body("Đăng xuất thành công");
  }
  // Endpoint test đã bỏ

}
