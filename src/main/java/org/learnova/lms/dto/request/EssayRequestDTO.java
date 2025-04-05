package org.learnova.lms.dto.request;

public class EssayRequestDTO  extends QuestionDTO{
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
