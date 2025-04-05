package org.learnova.lms.dto.response;

import java.time.LocalDate;

public record CourseResponseDTO(
        String title,
        LocalDate startTime,
        LocalDate endTime
) {
}
