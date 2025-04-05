package org.learnova.lms.repository;

import org.learnova.lms.domain.enums.ExamStatus;
import org.learnova.lms.domain.exam.ExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamSessionRepository extends JpaRepository<ExamSession, Long> {
    Optional<ExamSession> findByExam_IdAndStudent_IdAndStatus(Long examId, Long studentId, ExamStatus inProgress);
    Optional<ExamSession> findByExam_IdAndStudent_Id(Long examId, Long studentId);
}
