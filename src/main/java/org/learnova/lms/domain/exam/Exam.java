package org.learnova.lms.domain.exam;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.domain.base.BaseEntity;
import org.learnova.lms.domain.enums.ExamStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(indexes = {
        @Index(name = "idx_exam_date", columnList = "date"),
        @Index(name = "idx_exam_course", columnList = "course_id")
})
public class Exam extends BaseEntity<Long> {

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String description;

    //    @NotBlank
//    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExamStatus status = ExamStatus.NotStarted;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant startTime;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant endTime;

    @ManyToOne
    private Course course;

    @OneToOne
    private Teacher teacher;

    private String examTimeZone;

    @OneToMany(mappedBy = "exam")
//    @Fetch(FetchType.EAGER)
    private List<ExamQuestion> examQuestions;

    private Boolean shuffled;

    @OneToMany(mappedBy = "exam")
    private List<ExamSession> sessions;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<ExamQuestion> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(List<ExamQuestion> examQuestions) {
        this.examQuestions = examQuestions;
    }

    public Boolean getShuffled() {
        return shuffled;
    }

    public void setShuffled(Boolean shuffled) {
        this.shuffled = shuffled;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    public Double getPassingMark() {
        return passingMark;
    }

    public void setPassingMark(Double passingMark) {
        this.passingMark = passingMark;
    }

    private Double mark;

    private Double passingMark;

    public Exam(String title, String description, LocalDate date,
                Instant startTime, Instant endTime, String examTimeZone,
                ExamStatus notStarted, Course course, Teacher teacher,
                Double mark, Double passingMark) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
        this.examTimeZone = examTimeZone;
        this.status = notStarted;
        this.teacher = teacher;
        this.mark = mark;
        this.passingMark = passingMark;
    }

    public Exam() {

    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getExamTimeZone() {
        return examTimeZone;
    }

    public void setExamTimeZone(String examTimeZone) {
        this.examTimeZone = examTimeZone;
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

    public ExamStatus getStatus() {
        return status;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


}
