package com.eventplaner.model;


import com.eventplaner.model.auth.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String userName;
    private String location;
    private String bio;
    private String email;
    private UserRole role;
    private LocalDate registrationDate = LocalDate.now();


    @Column(nullable = false)
    private String password;

    public UserProfile(String fullName, String userName, String location,
                       String bio, String email, String password, UserRole role) {
        this.setFullName(fullName);
        this.setUserName(userName);
        this.setLocation(location);
        this.setBio(bio);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole(role);
    }

    public UserProfile() {}
}
