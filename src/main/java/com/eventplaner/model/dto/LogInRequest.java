package com.eventplaner.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LogInRequest(
        @NotBlank String userName,
        @NotBlank String password) {}
