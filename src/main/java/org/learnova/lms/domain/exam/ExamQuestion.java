package org.learnova.lms.domain.exam;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.learnova.lms.domain.base.BaseEntity;
import org.learnova.lms.domain.question.Question;

@Entity
public class ExamQuestion extends BaseEntity<Long> {

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private Question question;

    private Double score;


    public ExamQuestion(Long aLong) {
        super();
    }

    public ExamQuestion(Exam exam, Question question, Double score) {
        this.exam = exam;
        this.question = question;
        this.score = score;
    }


    public ExamQuestion() {
        super();
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
