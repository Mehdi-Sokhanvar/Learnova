package org.learnova.lms.service.question.questionFactory;


import org.learnova.lms.domain.enums.QType;
import org.learnova.lms.domain.question.Category;
import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.question.type_question.AnswerOption;
import org.learnova.lms.domain.question.type_question.EssayQuestion;
import org.learnova.lms.domain.question.type_question.MultipleOptionQuestion;
import org.learnova.lms.domain.question.type_question.TrueFalseQuestion;
import org.learnova.lms.dto.MultipleChoiceResponseDTO;
import org.learnova.lms.dto.OptionResponseDTO;
import org.learnova.lms.dto.QuestionResponseDTO;
import org.learnova.lms.dto.request.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MultipleChoiceQuestionFactory implements QuestionFactory {
    @Override
    public Question createQuestion(QuestionDTO dto) {
        MultipleChoiceRequestDTO questionDTO = (MultipleChoiceRequestDTO) dto;
        MultipleOptionQuestion question = new MultipleOptionQuestion();
        question.setTitle(questionDTO.getTitle());
        question.setDescription(questionDTO.getDescription());
        question.setDefaultScore(questionDTO.getDefaultScore());
        question.setIdentifier(questionDTO.getIdentifier());
        question.setLevel(questionDTO.getLevel());
        question.setCategory(new Category(dto.getCategory()));

        question.setShuffled(questionDTO.getShuffled());
        List<AnswerOption> answerOptions = new ArrayList<>();
        for (AnswerOptionDTO answerOption : questionDTO.getOptions()) {
            answerOptions.add(new AnswerOption(answerOption.getText(), answerOption.getIsCorrect()));
        }
        question.setOptions(answerOptions);

        question.setType(QType.MULTIPLE_CHOICE);
        return question;
    }

    @Override
    public boolean supports(String questionType) {
        return "MULTIPLE_CHOICE".equals(questionType);
    }

    @Override
    public void updateQuestion(Question existingQuestion, QuestionDTO dto) {

        MultipleChoiceRequestDTO multipleChoiceRequestDTO = (MultipleChoiceRequestDTO) dto;
        MultipleOptionQuestion multipleOptionQuestion = (MultipleOptionQuestion) existingQuestion;

        multipleOptionQuestion.setTitle(multipleChoiceRequestDTO.getTitle());
        multipleOptionQuestion.setDescription(multipleChoiceRequestDTO.getDescription());
        multipleOptionQuestion.setDefaultScore(multipleChoiceRequestDTO.getDefaultScore());
        multipleOptionQuestion.setIdentifier(multipleChoiceRequestDTO.getIdentifier());
        multipleOptionQuestion.setLevel(multipleChoiceRequestDTO.getLevel());
        multipleOptionQuestion.setCategory(new Category(dto.getCategory()));
        for (AnswerOptionDTO answerOptionDTO : multipleChoiceRequestDTO.getOptions()) {
            multipleOptionQuestion.getOptions().add(new AnswerOption(answerOptionDTO.getText(), answerOptionDTO.getIsCorrect()));
        }
    }

    @Override
    public QuestionResponseDTO convertToDto(Question question) {
        MultipleOptionQuestion dto = (MultipleOptionQuestion) question;
        MultipleChoiceResponseDTO optionDTO = new MultipleChoiceResponseDTO();
        optionDTO.setId(dto.getId());
        optionDTO.setTitle(dto.getTitle());
        optionDTO.setDescription(dto.getDescription());
        optionDTO.setScore(dto.getDefaultScore());
        optionDTO.setIdentifier(dto.getIdentifier());
        optionDTO.setLevel(dto.getLevel());
        optionDTO.setCategory(dto.getCategory().getName());
        for (AnswerOption answerOptionDTO:dto.getOptions())
        {
            optionDTO.getOptionResponseDTOList().add(new OptionResponseDTO(answerOptionDTO.getOption()));
        }
        return optionDTO;
    }
}
