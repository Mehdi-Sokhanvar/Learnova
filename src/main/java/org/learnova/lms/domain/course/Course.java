package org.learnova.lms.domain.course;

import jakarta.persistence.*;
import org.learnova.lms.domain.exam.Exam;
import org.learnova.lms.domain.base.BaseEntity;
import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.user.Student;
import org.learnova.lms.domain.user.Teacher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "idx_course_unique_id", columnList = "uniqueId", unique = true),
        @Index(name = "idx_course_start_date", columnList = "startDate"),
        @Index(name = "idx_course_end_date", columnList = "endDate")
})
public class Course extends BaseEntity<Long> {

    //todo : how to use high performance for queyr

    private String title;

    private String description;
    //todo: what is uuid
    private UUID uniqueId = UUID.randomUUID();

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    private Teacher teacher;

    @ManyToMany(mappedBy = "courseList", fetch = FetchType.LAZY)
    private List<Student> studentList;


    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Exam> examList = new ArrayList<>();


    @OneToMany(mappedBy = "course")
    private List<Question>  questions;


    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public Course(String title, String description, LocalDate startDate, LocalDate endDate, UUID uniqueId) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.uniqueId = uniqueId;
    }

    public Course() {

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public List<Exam> getExamList() {
        return examList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }



    public void setExamList(List<Exam> examList) {
        this.examList = examList;
    }

    //    public Course(@NotEmpty(message = "name Course has some error  ") @NotBlank(message = "name Of Course is empty") String name, String description, LocalDate startDate, LocalDate endDate, UUID uuid) {
//        super();
//    }
}
//todo : what is localdata and time in java
//    todo: what is uuid
// todo :embedble and emvbeede
