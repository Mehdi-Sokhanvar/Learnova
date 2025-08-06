package org.learnova.lms.dto.request;

import javax.validation.constraints.*;
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
