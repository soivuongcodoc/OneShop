package com.oneshop.service;

import com.oneshop.dto.AuthDtos.*;
import com.oneshop.entity.OtpCode;
import com.oneshop.entity.Role;
import com.oneshop.entity.User;
import com.oneshop.repository.OtpCodeRepository;
import com.oneshop.repository.RoleRepository;
import com.oneshop.repository.UserRepository;
import com.oneshop.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Service @RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final OtpCodeRepository otpRepo;
  private final BCryptPasswordEncoder encoder;
  private final MailService mailService;
  private final JwtTokenProvider jwt;

  public void register(RegisterRequest req) {
    if (userRepo.existsByUsername(req.getUsername())) throw new RuntimeException("Username existed");
    if (userRepo.existsByEmail(req.getEmail())) throw new RuntimeException("Email existed");

    Role userRole = roleRepo.findByName("ROLE_USER")
        .orElseGet(() -> roleRepo.save(Role.builder().name("ROLE_USER").build()));

    User user = User.builder()
        .username(req.getUsername())
        .email(req.getEmail())
        .password(encoder.encode(req.getPassword()))
        .enabled(false)
        .roles(Set.of(userRole))
        .build();
    userRepo.save(user);

    String code = genOtp();
    otpRepo.save(OtpCode.builder()
        .email(req.getEmail())
        .code(code)
        .type(OtpCode.OtpType.VERIFY_EMAIL)
        .expiresAt(LocalDateTime.now().plusMinutes(10))
        .used(false)
        .build());

    mailService.sendOtp(req.getEmail(), "OneShop - Verify your email", code);
  }

  public void verifyEmail(VerifyEmailRequest req) {
    var otp = otpRepo.findTopByEmailAndTypeAndUsedFalseOrderByIdDesc(
        req.getEmail(), OtpCode.OtpType.VERIFY_EMAIL
    ).orElseThrow(() -> new RuntimeException("OTP not found"));

    if (otp.isUsed() || LocalDateTime.now().isAfter(otp.getExpiresAt()))
      throw new RuntimeException("OTP expired/used");

    if (!otp.getCode().equals(req.getOtp()))
      throw new RuntimeException("OTP invalid");

    otp.setUsed(true);
    otpRepo.save(otp);

    User u = userRepo.findByEmail(req.getEmail()).orElseThrow();
    u.setEnabled(true);
    userRepo.save(u);
  }

  public JwtResponse login(LoginRequest req) {
    // Cho phép login bằng username hoặc email
	  User u = userRepo.findByUsername(req.getUsernameOrEmail())
			    .or(() -> userRepo.findByEmail(req.getUsernameOrEmail()))
			    .orElseThrow(() -> new RuntimeException("Account not found"));
    if (u == null || !u.isEnabled()) throw new RuntimeException("Account not found or not verified");
    if (!encoder.matches(req.getPassword(), u.getPassword())) throw new RuntimeException("Wrong credentials");

    String subject = u.getUsername(); // subject của JWT
    String token = jwt.generateToken(subject);
    return new JwtResponse(token, "Bearer", u.getUsername());
  }

  public void forgotPassword(ForgotPasswordRequest req) {
    User u = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("Email not found"));
    String code = genOtp();
    otpRepo.save(OtpCode.builder()
        .email(u.getEmail())
        .code(code)
        .type(OtpCode.OtpType.RESET_PASSWORD)
        .expiresAt(LocalDateTime.now().plusMinutes(10))
        .used(false)
        .build());
    mailService.sendOtp(u.getEmail(), "OneShop - Reset password OTP", code);
  }

  public void resetPassword(ResetPasswordRequest req) {
    var otp = otpRepo.findTopByEmailAndTypeAndUsedFalseOrderByIdDesc(
        req.getEmail(), OtpCode.OtpType.RESET_PASSWORD
    ).orElseThrow(() -> new RuntimeException("OTP not found"));

    if (otp.isUsed() || LocalDateTime.now().isAfter(otp.getExpiresAt()))
      throw new RuntimeException("OTP expired/used");

    if (!otp.getCode().equals(req.getOtp()))
      throw new RuntimeException("OTP invalid");

    otp.setUsed(true);
    otpRepo.save(otp);

    User u = userRepo.findByEmail(req.getEmail()).orElseThrow();
    u.setPassword(encoder.encode(req.getNewPassword()));
    userRepo.save(u);
  }

  private String genOtp() {
    return String.format("%06d", new Random().nextInt(1_000_000));
  }
}
