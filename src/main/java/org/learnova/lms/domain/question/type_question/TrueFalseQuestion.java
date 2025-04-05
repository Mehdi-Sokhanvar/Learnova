package org.learnova.lms.domain.question.type_question;

import jakarta.persistence.Entity;
import org.learnova.lms.domain.question.Question;


@Entity
public class TrueFalseQuestion extends Question {
    private Boolean isCorrect;

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }
}
