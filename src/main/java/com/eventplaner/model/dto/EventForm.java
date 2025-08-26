package com.eventplaner.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EventForm(@NotBlank @Size(max = 128) String title,
                        @NotBlank @Size(max = 5000) String content) {
}
