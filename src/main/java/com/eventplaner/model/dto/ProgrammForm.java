package com.eventplaner.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProgrammForm(@NotBlank @Size(max = 64, min = 4) String title,
                           @NotBlank @Size(max = 1000, min = 4) String content) {
}
