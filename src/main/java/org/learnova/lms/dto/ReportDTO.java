package org.learnova.lms.dto;

public record ReportDTO(
        Long studentId,
        String studentName,
        Long examId,
        String examName,
        Double score
) {
}
