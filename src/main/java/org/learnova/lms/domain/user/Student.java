package org.learnova.lms.domain.user;

import jakarta.persistence.*;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.exam.ExamSession;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends AppUser{

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"),
            indexes = {
                    @Index(name = "idx_course_student_student", columnList = "student_id"),
                    @Index(name = "idx_course_student_course", columnList = "course_id")
            }
    )
    private List<Course> courseList=new ArrayList<>();


    @OneToMany(mappedBy = "student")
    private List<ExamSession> examSessions;


    public Student(String userName, String password, String email, Role role) {
        super(userName, password, email, role);
    }


    public Student() {

    }


    public List<Course> getCourseList() {
        return courseList;
    }
}
//read documnt of chat pg

//todo : think a bout this sysntax
//todo : وقتی جدول های شما چندین رابطخ داشته باشه و شما  توی برنماه تون mapped بای نزاری چدل اضافه میزاره