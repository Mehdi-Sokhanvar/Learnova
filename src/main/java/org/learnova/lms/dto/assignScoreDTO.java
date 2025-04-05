package org.learnova.lms.dto;

public record assignScoreDTO(
        Long StudentId,
        Long questionId,
        Double score
) {
}
