package org.learnova.lms.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record ExamResponseDTO(
        String title,
        String description,
        LocalDate date,
        Instant startTime,
        Instant endTime,
        String courseName
) {
}
