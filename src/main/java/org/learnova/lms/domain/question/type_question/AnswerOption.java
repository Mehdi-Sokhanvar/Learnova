package org.learnova.lms.domain.question.type_question;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.learnova.lms.domain.base.BaseEntity;
import org.learnova.lms.domain.question.Question;

@Entity
public class AnswerOption extends BaseEntity<Long> {

    @ManyToOne
    private MultipleOptionQuestion multipleOptionQuestion;
    private String option;
    private Boolean isCorrect;

    public AnswerOption(String option, Boolean isCorrect) {
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public AnswerOption(String option) {
        this.option = option;
    }

    public AnswerOption() {

    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
