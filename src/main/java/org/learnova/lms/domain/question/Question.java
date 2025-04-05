package org.learnova.lms.domain.question;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import org.learnova.lms.domain.base.BaseEntity;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.enums.QType;
import org.learnova.lms.domain.enums.QuestionLevel;
import org.learnova.lms.domain.exam.ExamQuestion;
import org.learnova.lms.domain.user.Teacher;

import java.util.List;

@Entity
@Inheritance(
        strategy =
                InheritanceType.JOINED)
@Table(indexes = {
        @Index(name = "idx_question_identifier", columnList = "identifier"),
        @Index(name = "idx_question_course", columnList = "course_id")
})
public class Question extends BaseEntity<Long> {


    @Column(length = 255)
//    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
//    @NotNull
    private String description;

    @Column(unique = true)
    private String identifier;


    @Column()
    @Positive
    private Double defaultScore;

    @Enumerated(
            EnumType.STRING)
    private QuestionLevel level;

    @OneToOne(
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE})
    private Category category;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(
            mappedBy = "question")
    private List<ExamQuestion> examQuestions;

    @ManyToOne
    private Course course;

    @Enumerated(EnumType.STRING)
    private QType type;

    private Boolean isDeleted = false;


    public QType getType() {
        return type;
    }

    public void setType(QType type) {
        this.type = type;
    }


    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
