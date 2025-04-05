package org.learnova.lms.dto;

import java.util.List;

public class MultipleChoiceResponseDTO extends QuestionResponseDTO {
    List<OptionResponseDTO> optionResponseDTOList;

    public List<OptionResponseDTO> getOptionResponseDTOList() {
        return optionResponseDTOList;
    }

    public void setOptionResponseDTOList(List<OptionResponseDTO> optionResponseDTOList) {
        this.optionResponseDTOList = optionResponseDTOList;
    }
}
