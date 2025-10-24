package com.oneshop.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class AuthDtos {
  @Getter @Setter
  public static class RegisterRequest {
    @NotBlank @Size(min=3, max=100)
    private String username;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min=6, max=100)
    private String password;
    private String fullName;
  }

  @Getter @Setter
  public static class LoginRequest {
    @NotBlank
    private String usernameOrEmail;
    @NotBlank
    private String password;
  }

  @Getter @Setter
  public static class VerifyEmailRequest {
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min=4, max=10)
    private String otp;
  }

  @Getter @Setter
  public static class ForgotPasswordRequest {
    @NotBlank @Email
    private String email;
  }

  @Getter @Setter
  public static class ResetPasswordRequest {
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min=4, max=10)
    private String otp;
    @NotBlank @Size(min=6, max=100)
    private String newPassword;
  }

  @Getter @Setter @AllArgsConstructor
  public static class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private String role;
  }
}
