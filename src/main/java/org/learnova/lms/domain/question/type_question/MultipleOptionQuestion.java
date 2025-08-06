package org.learnova.lms.domain.question.type_question;


import org.learnova.lms.domain.question.Question;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
