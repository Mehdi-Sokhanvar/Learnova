package org.learnova.lms.domain.exam;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.Type;
import org.learnova.lms.domain.base.BaseEntity;
import org.learnova.lms.domain.enums.ExamStatus;
import org.learnova.lms.domain.user.Student;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
public class ExamSession extends BaseEntity<Long> {

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private Student student;

    private Instant startTime;

    private Instant endTime;

    private ExamStatus status;

    private Double totalScore;


    @ElementCollection
    private List<Long> questionOrder;

    @OneToMany(mappedBy = "examSession")
    private List<StudentAnswer> studentAnswers;


    private Integer currentQuestion;

    public ExamSession(Exam exam, Student student, Instant startTime, Instant endTime, ExamStatus status, Double totalScore, List<Long> questionOrder, List<StudentAnswer> studentAnswers, Integer currentQuestion) {
        this.exam = exam;
        this.student = student;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalScore = totalScore;
        this.questionOrder = questionOrder;
        this.studentAnswers = studentAnswers;
        this.currentQuestion = currentQuestion;
    }

    public ExamSession() {

    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }

    public void setQuestionOrder(List<Long> questionOrder) {
        this.questionOrder = questionOrder;
    }

    public void setCurrentQuestion(Integer currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public Exam getExam() {
        return exam;
    }

    public Student getStudent() {
        return student;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public ExamStatus getStatus() {
        return status;
    }

    public List<Long> getQuestionOrder() {
        return questionOrder;
    }

    public List<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }

    public Integer getCurrentQuestion() {
        return currentQuestion;
    }
}
