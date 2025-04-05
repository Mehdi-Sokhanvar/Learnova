package org.learnova.lms.service.question.questionFactory;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class QuestionFactoryProvider {

    private List<QuestionFactory> factories;

    public QuestionFactoryProvider(List<QuestionFactory> factories) {
        this.factories = factories;
    }

    public QuestionFactory getFactory(String questionType) {
        return factories.stream()
                .filter(factories -> factories.supports(questionType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unsupported question type: " + questionType)
                );
    }
}
