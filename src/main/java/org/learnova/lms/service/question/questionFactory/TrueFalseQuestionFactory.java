package org.learnova.lms.service.question.questionFactory;

import org.learnova.lms.domain.enums.QType;
import org.learnova.lms.domain.question.Category;
import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.question.type_question.AnswerOption;
import org.learnova.lms.domain.question.type_question.EssayQuestion;
import org.learnova.lms.domain.question.type_question.MultipleOptionQuestion;
import org.learnova.lms.domain.question.type_question.TrueFalseQuestion;
import org.learnova.lms.dto.QuestionResponseDTO;
import org.learnova.lms.dto.TrueFalseResponseDTO;
import org.learnova.lms.dto.request.*;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class TrueFalseQuestionFactory implements QuestionFactory {
    @Override
    public Question createQuestion(QuestionDTO dto) {
        TrueFalseRequestDTO questionDTO = (TrueFalseRequestDTO) dto;
        TrueFalseQuestion question = new TrueFalseQuestion();
        question.setTitle(questionDTO.getTitle());
        question.setDescription(questionDTO.getDescription());
        question.setDefaultScore(questionDTO.getDefaultScore());
        question.setIdentifier(UUID.randomUUID().toString());
        question.setLevel(questionDTO.getLevel());
        question.setCategory(new Category(dto.getCategory()));
        question.setCorrect(questionDTO.getIsCorrect());
        question.setType(QType.TRUE_FALSE);
        return question;
    }

    @Override
    public boolean supports(String questionType) {
        return "TRUE_FALSE".equals(questionType);
    }

    @Override
    public void updateQuestion(Question existingQuestion, QuestionDTO dto) {
        TrueFalseRequestDTO trueFalseDTO=(TrueFalseRequestDTO) dto;
        TrueFalseQuestion trueFalseQuestion=(TrueFalseQuestion) existingQuestion;

        trueFalseQuestion.setTitle(trueFalseDTO.getTitle());
        trueFalseQuestion.setDescription(trueFalseDTO.getDescription());
        trueFalseQuestion.setDefaultScore(trueFalseDTO.getDefaultScore());
        trueFalseQuestion.setIdentifier(UUID.randomUUID().toString());
        trueFalseQuestion.setLevel(trueFalseDTO.getLevel());
        trueFalseQuestion.setCategory(new Category(dto.getCategory()));
        trueFalseQuestion.setCorrect(trueFalseDTO.getIsCorrect());
    }

    @Override
    public QuestionResponseDTO convertToDto(Question question) {

        TrueFalseQuestion dto = (TrueFalseQuestion) question;
        TrueFalseResponseDTO optionDTO = new TrueFalseResponseDTO();
        optionDTO.setId(dto.getId());
        optionDTO.setTitle(dto.getTitle());
        optionDTO.setDescription(dto.getDescription());
        optionDTO.setScore(dto.getDefaultScore());
        optionDTO.setIdentifier(dto.getIdentifier());
        optionDTO.setLevel(dto.getLevel());
        optionDTO.setCategory(dto.getCategory().getName());
        return optionDTO;
    }
}
