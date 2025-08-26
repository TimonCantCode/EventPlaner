package com.eventplaner.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record SignUpRequest(
        @NotBlank String userName,
        @NotBlank String fullName,
        @NotBlank String email,
        String location,
        String bio,
        @NotBlank @Size(min = 8) String password) { }
