package org.learnova.lms.dto.response;

public record StudentResponse(
        String username,
        String email,
        String firstName,
        String lastName
) {
}
