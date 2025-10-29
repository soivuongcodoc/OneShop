package com.oneshop.controller.auth;

import com.oneshop.dto.auth.AuthDtos.VerifyEmailRequest;
import com.oneshop.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class VerifyController {

    private final AuthService authService;

    // Xử lý khi người dùng nhập OTP và nhấn xác minh
    @PostMapping("/verify")
    public String verifyEmail(@ModelAttribute VerifyEmailRequest req, Model model) {
        try {
            authService.verifyEmail(req);
            model.addAttribute("success", "Xác thực email thành công! Hãy đăng nhập.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/verify";
        }
    }
}
