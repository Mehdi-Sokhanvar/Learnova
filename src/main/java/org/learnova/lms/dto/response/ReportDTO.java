package org.learnova.lms.dto.response;

public record ReportDTO(
        Long studentId,
        String studentName,
        Long examId,
        String examName,
        Double score
) {
}
