package org.learnova.lms.dto.request;

import jakarta.validation.constraints.*;

public record RegisterDTO(
        @NotNull
        @NotBlank
        @Email
        String email,
        @NotNull
        @NotBlank
        @Size(min = 8)
//                @Max() for number
        String password
) {
}
