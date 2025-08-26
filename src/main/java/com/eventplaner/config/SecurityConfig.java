package com.eventplaner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/all-users").hasRole("ADMIN") // zugriff auf diese pfade ist nur für admins erlaubt
                        .requestMatchers(
                                "/register", "/login", "/signup_error",
                                "/auth/register", "/auth/login",
                                "/", "*.css", "/posts/all-posts",
                                "/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/", true))
                .csrf(csrf -> csrf.
                        ignoringRequestMatchers("/h2-console/**", "/auth/register",
                                "auth/login"))
                .logout(logout -> logout.logoutUrl("/logout") // standard post logout controller. wird von spring bereitgestellt
                        .logoutSuccessUrl("/login")   // ziel url nach erfolgreichen logout
                        .invalidateHttpSession(true) // beende die session
                        .clearAuthentication(true) // lösche die authentifizierung
                        .deleteCookies("JSESSIONID") // lösche das cookie
                        .permitAll()); // erlaube allen users sich auszuloggen
        return http.build();
    }

}
