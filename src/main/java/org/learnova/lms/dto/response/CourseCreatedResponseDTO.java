package org.learnova.lms.dto.response;

import java.time.LocalDate;

public record CourseCreatedResponseDTO(
        String message,
        String title,
        LocalDate startTime,
        LocalDate endTime
) {
}
