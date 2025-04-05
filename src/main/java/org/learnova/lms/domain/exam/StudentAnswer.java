package org.learnova.lms.domain.exam;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.learnova.lms.domain.base.BaseEntity;

@Entity
public class StudentAnswer extends BaseEntity<Long> {

    @ManyToOne
    private ExamSession examSession;

    @ManyToOne
    private ExamQuestion examQuestion;

    private String answer;

    private Double score;


    private String feedback;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ExamSession getExamSession() {
        return examSession;
    }

    public void setExamSession(ExamSession examSession) {
        this.examSession = examSession;
    }

    public ExamQuestion getExamQuestion() {
        return examQuestion;
    }

    public void setExamQuestion(ExamQuestion examQuestion) {
        this.examQuestion = examQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
