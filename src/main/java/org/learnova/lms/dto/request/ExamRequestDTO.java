package org.learnova.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ExamRequestDTO(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        @NotBlank
        String description,
        @NotNull
        @NotBlank
        String date,
        @NotNull
        @NotBlank
        LocalDateTime startTime,
        @NotNull
        LocalDateTime  endTime,
        @NotNull
        @NotBlank
        Long courseId,
        @NotNull
        String examTimeZone,
        Double mark,
        Double passiveMark
) {
}
