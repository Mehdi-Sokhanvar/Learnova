package org.learnova.lms.dto;

public record AnswerQuestionDTO(
        String question,
        String answer,
        Double score
) {
}
