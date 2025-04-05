package org.learnova.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.learnova.lms.domain.enums.Status;

public record UserRequestDTO(
        @NotBlank
        @NotNull
        String username,
        @NotBlank
        @NotNull
        String firstName,
        @NotBlank
        @NotNull
        String lastName,
        @NotBlank
        @NotNull
        String email,
        @NotBlank
        @NotNull
        String phone,
        @NotBlank
        @NotNull
        Status status
) {
}
