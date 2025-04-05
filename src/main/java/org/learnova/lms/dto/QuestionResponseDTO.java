package org.learnova.lms.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.learnova.lms.domain.enums.QuestionLevel;
import org.learnova.lms.dto.request.EssayRequestDTO;
import org.learnova.lms.dto.request.MultipleChoiceRequestDTO;
import org.learnova.lms.dto.request.TrueFalseRequestDTO;

@JsonTypeInfo(
        use=JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EssayResponseDTO.class, name = "ESSAY"),
        @JsonSubTypes.Type(value = MultipleChoiceResponseDTO.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = TrueFalseResponseDTO.class,name = "TRUE_FALSE")
})
public class QuestionResponseDTO {



    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String title;
    private String description;
    private String identifier;
    private Double score;
    private QuestionLevel level;
    private String category;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double defaultScore) {
        this.score = defaultScore;
    }

    public QuestionLevel getLevel() {
        return level;
    }

    public void setLevel(QuestionLevel level) {
        this.level = level;
    }

}
