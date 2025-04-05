package org.learnova.lms.service.question.questionFactory;

import org.learnova.lms.domain.question.Question;
import org.learnova.lms.dto.request.QuestionDTO;
import org.learnova.lms.dto.QuestionResponseDTO;

public interface QuestionFactory {

    Question createQuestion(QuestionDTO dto);
    boolean supports(String questionType);
    void updateQuestion(Question existingQuestion, QuestionDTO dto);
    QuestionResponseDTO convertToDto(Question question);
}
