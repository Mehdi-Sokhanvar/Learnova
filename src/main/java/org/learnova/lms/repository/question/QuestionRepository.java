package org.learnova.lms.repository.question;

import org.learnova.lms.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {


    @Query("SELECT q FROM Question q WHERE q.teacher.id= :teacher_id AND q.course.id= :course_id")
    List<Question> findQuestionInCourse(@Param("teacher_id") Long teacherId, @Param("course_id") Long courseId);


    List<Question> findQuestionByTeacher_Id(Long teacherId);

}
