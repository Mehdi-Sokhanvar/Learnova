package org.learnova.lms.domain.question.type_question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import org.learnova.lms.domain.question.Question;

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
