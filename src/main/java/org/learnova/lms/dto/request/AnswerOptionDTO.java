package org.learnova.lms.dto.request;

public class AnswerOptionDTO {
    private String text;
    private Boolean isCorrect;

    public AnswerOptionDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean correct) {
        isCorrect = correct;
    }
}
