package com.dblayer.controller.auth;

import com.dblayer.model.UserProfile;
import com.dblayer.model.dto.LogInRequest;
import com.dblayer.model.dto.SignUpRequest;
import com.dblayer.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {this.authService = authService;}

    @PostMapping("/register")
    public String signup(@ModelAttribute SignUpRequest dto) {
        try {
            this.authService.register(dto);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            return "redirect:/signup_error";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LogInRequest dto, HttpServletRequest request) {
        try {
            this.authService.login(dto, request);
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            return "redirect:/signup_error";
        }
    }

}
