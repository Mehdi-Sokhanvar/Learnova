package org.learnova.lms.dto.response;

import java.time.Instant;

public record ExamSessionResponseDTO(
        Long studentId,
        Long examId,
        Instant startTime,
        Integer currentQuestion,
        String message
) {
}
