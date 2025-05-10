package org.learnova.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ExamRequestDTO(
        String title,
        String description,
        String date,
        LocalDateTime startTime,
        LocalDateTime  endTime,
        Long courseId,
        String examTimeZone,
        Double mark,
        Double passiveMark
) {
}
