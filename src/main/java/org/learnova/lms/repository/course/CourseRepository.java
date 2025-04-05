package org.learnova.lms.repository.course;

import org.learnova.lms.domain.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

//    List<Course> findCourseByTeacher_id(Long teacher_id);

    @Query("SELECT c FROM Course c WHERE c.teacher.id = :id")
    List<Course> findCourseByTeacherId(@Param("id") Long teacherId);;


}

//todo : کلاسها و ایترفیس هایی که به ما اضافه میکنه
