package org.learnova.lms.repository;

import org.learnova.lms.domain.exam.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    Optional<StudentAnswer> findStudentAnswerByExamSession_IdAndExamQuestion_Id(Long sessionId, Long question_Id);

}
