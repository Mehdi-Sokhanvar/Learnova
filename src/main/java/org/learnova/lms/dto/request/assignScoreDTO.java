package org.learnova.lms.dto.request;

public record assignScoreDTO(
        Long StudentId,
        Long questionId,
        Double score
) {
}
