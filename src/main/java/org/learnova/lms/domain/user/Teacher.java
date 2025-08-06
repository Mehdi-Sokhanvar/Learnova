package org.learnova.lms.domain.user;


import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.question.Question;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Teacher extends AppUser{

    @OneToMany(mappedBy = "teacher")
    private List<Course> courseList;


    @OneToMany
    private List<Question> questionList;

    public Teacher(String userName, String password, String email, Role role) {
        super(userName, password, email, role);
    }


    public Teacher(Long teacherId) {
        this.setId(teacherId);
    }

    public Teacher() {

    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
