package org.learnova.lms.dto.response;

import org.learnova.lms.domain.enums.Status;

public record UserResponseDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        Status status
) {
}
