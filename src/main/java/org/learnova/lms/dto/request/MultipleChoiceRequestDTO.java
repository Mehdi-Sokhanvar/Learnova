package org.learnova.lms.dto.request;

import java.util.List;

public class MultipleChoiceRequestDTO extends QuestionDTO {

    private List<AnswerOptionDTO> options;
    private Boolean shuffled;

    public List<AnswerOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOptionDTO> options) {
        this.options = options;
    }

    public Boolean getShuffled() {
        return shuffled;
    }

    public void setShuffled(Boolean shuffled) {
        this.shuffled = shuffled;
    }
}
