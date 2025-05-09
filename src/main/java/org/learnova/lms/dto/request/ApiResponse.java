package org.learnova.lms.dto.request;

public record ApiResponse(
        boolean success,
        String message
) {
}
