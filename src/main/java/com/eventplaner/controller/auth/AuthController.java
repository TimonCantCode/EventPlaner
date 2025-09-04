package com.eventplaner.controller.auth;

import com.eventplaner.model.UserProfile;
import com.eventplaner.model.dto.LogInRequest;
import com.eventplaner.model.dto.SignUpRequest;
import com.eventplaner.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {this.authService = authService;}

    @PostMapping("/register")
    public String signup(@ModelAttribute SignUpRequest dto) {
        try {
            this.authService.register(dto);
            logger.info("Registrierung erfolgreich für Benutzer: {}", dto.userName());
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            logger.error("Registrierung fehlgeschlagen für Benutzer: {}", dto.userName());
            return "redirect:/signup_error";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LogInRequest dto, HttpServletRequest request) {
        try {
            this.authService.login(dto, request);
            logger.info("Login erfolgreich für Benutzer: {}", dto.userName());
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            logger.error("Login fehlgeschlagen für Benutzer: {}", dto.userName());
            return "redirect:/signup_error";
        }
    }

}
