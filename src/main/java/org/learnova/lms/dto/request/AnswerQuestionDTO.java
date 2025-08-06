package org.learnova.lms.dto.request;

public record AnswerQuestionDTO(
        String question,
        String answer,
        Double score
) {
}
