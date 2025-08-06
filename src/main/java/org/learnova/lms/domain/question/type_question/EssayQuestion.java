package org.learnova.lms.domain.question.type_question;


import org.learnova.lms.domain.question.Question;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class EssayQuestion  extends Question {



    @Column(nullable = false, length = 255)
    @NotNull
    private String answer;


    private Integer maxLength;



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
