package com.oneshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class MailService {
  private final JavaMailSender mailSender;

  public void sendOtp(String to, String subject, String otp) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject(subject);
    msg.setText("Your OTP code is: " + otp + "\nThis code will expire in 10 minutes.");
    mailSender.send(msg);
  }
}
