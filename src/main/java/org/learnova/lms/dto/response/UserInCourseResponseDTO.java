package org.learnova.lms.dto.response;

public record UserInCourseResponseDTO(
        Long id,
        String email,
        String userName,
        String role
) {
}
