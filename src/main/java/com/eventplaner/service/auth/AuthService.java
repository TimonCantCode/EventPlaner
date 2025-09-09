package com.eventplaner.service.auth;

import com.eventplaner.model.UserProfile;
import com.eventplaner.model.auth.UserRole;
import com.eventplaner.model.dto.LogInRequest;
import com.eventplaner.model.dto.SignUpRequest;
import com.eventplaner.repository.UserProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserProfileRepository userProfileRepository,
                       PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfile register(SignUpRequest dto) {
        if (this.userProfileRepository.findByUserName(dto.userName()).isPresent()) {
            throw new IllegalArgumentException("User name already exists");
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setPassword(passwordEncoder.encode(dto.password()));
        userProfile.setBio(dto.bio());
        userProfile.setEmail(dto.email());
        userProfile.setUserName(dto.userName());
        userProfile.setFullName(dto.fullName());
        userProfile.setLocation(dto.location());
        if (userProfile.getEmail().contains("@veranstalter.com")) {
            userProfile.setRole(UserRole.ROLE_ADMIN);
        } else {
            userProfile.setRole(UserRole.ROLE_USER);
        }
        return this.userProfileRepository.save(userProfile);
    }

    public UserProfile authenticate(String userName, String password) {
        UserProfile userProfile = this.userProfileRepository.findByUserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("User name not found"));

        if (!this.passwordEncoder.matches(password, userProfile.getPassword())) {
            throw new IllegalArgumentException("Password does not match");
        }

        return userProfile;
    }

    public UserProfile login(LogInRequest dto, HttpServletRequest request) {

        UserProfile user = this.authenticate(dto.userName(), dto.password());

        // Security Context und Session werden erstellt und gesetzt
        // 1. Authentifizierungstoken setzen. Da steht drin:
        // wer bin ich? -> steht im principal object, hier der userName
        // was darf ich? -> wird durch die rolle beschrieben, hier ROLE_USER. es gibt auch ROLE_ADMIN
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getUserName(),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().name())) // liest die rollen Enum aus und konvertiert zu einem String
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext(); // hole neuen, leeren context
        context.setAuthentication(authToken); //  schreibt token in den context
        SecurityContextHolder.setContext(context); // setzt den context als aktuellen security context

        // zum schluss: request abarbeiten und session eröffnen
        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        return user;
    }



}
