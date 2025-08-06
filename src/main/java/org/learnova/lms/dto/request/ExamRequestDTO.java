package org.learnova.lms.dto.request;

import javax.validation.constraints.*;

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
