package org.learnova.lms.repository.exam;

import org.learnova.lms.domain.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT e FROM Exam e WHERE e.course.id = :course")
    List<Exam> findExamByCourse(Long course);

}
