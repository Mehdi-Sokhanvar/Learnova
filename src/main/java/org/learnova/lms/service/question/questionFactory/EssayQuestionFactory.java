package org.learnova.lms.service.question.questionFactory;

import org.learnova.lms.domain.enums.QType;
import org.learnova.lms.domain.question.Category;
import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.question.type_question.EssayQuestion;
import org.learnova.lms.dto.EssayResponseDTO;
import org.learnova.lms.dto.request.EssayRequestDTO;
import org.learnova.lms.dto.request.QuestionDTO;
import org.learnova.lms.dto.QuestionResponseDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EssayQuestionFactory implements QuestionFactory {
    @Override
    public Question createQuestion(QuestionDTO dto) {
        EssayRequestDTO questionDTO = (EssayRequestDTO) dto;
        EssayQuestion question = new EssayQuestion();
        question.setTitle(questionDTO.getTitle());
        question.setDescription(questionDTO.getDescription());
        question.setDefaultScore(questionDTO.getDefaultScore());
        question.setIdentifier(UUID.randomUUID().toString());
        question.setLevel(questionDTO.getLevel());
        question.setCategory(new Category(dto.getCategory()));
        question.setAnswer(questionDTO.getAnswer());
        question.setMaxLength(questionDTO.getMaxLength());
        question.setType(QType.ESSAY);
        return question;
    }

    @Override
    public boolean supports(String questionType) {
        return "ESSAY".equals(questionType);
    }

    @Override
    public void updateQuestion(Question existingQuestion, QuestionDTO dto) {
        EssayRequestDTO essayDTO = (EssayRequestDTO) dto;
        EssayQuestion essayQuestion = (EssayQuestion) existingQuestion;

        essayQuestion.setTitle(essayDTO.getTitle());
        essayQuestion.setDescription(essayDTO.getDescription());
        essayQuestion.setDefaultScore(essayDTO.getDefaultScore());
        essayQuestion.setIdentifier(UUID.randomUUID().toString());
        essayQuestion.setLevel(essayDTO.getLevel());
        essayQuestion.setCategory(new Category(dto.getCategory()));
        essayQuestion.setAnswer(essayDTO.getAnswer());
        essayQuestion.setMaxLength(essayDTO.getMaxLength());
    }

    @Override
    public QuestionResponseDTO convertToDto(Question question) {
        EssayQuestion dto = (EssayQuestion) question;

        EssayResponseDTO essayDTO = new EssayResponseDTO();
        essayDTO.setId(dto.getId());
        essayDTO.setTitle(dto.getTitle());
        essayDTO.setDescription(dto.getDescription());
        essayDTO.setScore(dto.getDefaultScore());
        essayDTO.setIdentifier(dto.getIdentifier());
        essayDTO.setLevel(dto.getLevel());
        essayDTO.setCategory(dto.getCategory().getName());
        return essayDTO;
    }
}
