package org.learnova.lms.dto;

import org.learnova.lms.domain.enums.ExamStatus;

import java.time.Instant;
import java.util.List;

public record TeacherReportAsStudent(
        Long student,
        String studentName,
        Double score,
        Instant startTime,
        Instant endTime,
        ExamStatus examStatus,
        List<AllAnswersQuestionDTO> allAnswersQuestionDTO
) {
}
