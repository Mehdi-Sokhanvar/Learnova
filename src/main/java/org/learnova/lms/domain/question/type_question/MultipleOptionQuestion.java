package org.learnova.lms.domain.question.type_question;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.learnova.lms.domain.question.Question;

import java.util.List;


@Entity
public class MultipleOptionQuestion extends Question {

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "multipleOptionQuestion")
    private List<AnswerOption> options;
    private Boolean shuffled;

    public List<AnswerOption> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOption> options) {
        this.options = options;
    }

    public Boolean getShuffled() {
        return shuffled;
    }

    public void setShuffled(Boolean shuffled) {
        this.shuffled = shuffled;
    }
}
