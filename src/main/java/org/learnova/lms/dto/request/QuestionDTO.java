package org.learnova.lms.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.learnova.lms.domain.enums.QuestionLevel;

@JsonTypeInfo(
        use=JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EssayRequestDTO.class, name = "ESSAY"),
        @JsonSubTypes.Type(value = MultipleChoiceRequestDTO.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = TrueFalseRequestDTO.class,name = "TRUE_FALSE")
})

public class QuestionDTO {

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
    private Double defaultScore;
    private QuestionLevel level;
    private String category;
    private Long CourseId;
    private String dtype;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getdtype() {
        return dtype;
    }

    public void setdtype(String type) {
        this.dtype = type;
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

    public Double getDefaultScore() {
        return defaultScore;
    }

    public void setDefaultScore(Double defaultScore) {
        this.defaultScore = defaultScore;
    }

    public QuestionLevel getLevel() {
        return level;
    }

    public void setLevel(QuestionLevel level) {
        this.level = level;
    }



    public Long getCourseId() {
        return CourseId;
    }

    public void setCourseId(Long courseId) {
        CourseId = courseId;
    }
}
