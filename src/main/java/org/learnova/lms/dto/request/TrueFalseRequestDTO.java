package org.learnova.lms.dto.request;

public class TrueFalseRequestDTO extends QuestionDTO{
    private Boolean isCorrect;

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean correct) {
        isCorrect = correct;
    }
}
