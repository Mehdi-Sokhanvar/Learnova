package org.learnova.lms.repository.question;

import org.learnova.lms.domain.exam.ExamQuestion;
import org.learnova.lms.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

//    ExamQuestion findByQuestion_idAndExam_id(ID questionId, ID examId);

    @Query("SELECT q FROM ExamQuestion q WHERE q.exam.id= :exam_id AND   q.question.id= :question_id")
    Optional<ExamQuestion> findByQuestionAndExam(@Param("question_id") Long question_id, @Param("exam_id") Long exam_id);

    @Query("SELECT eq FROM ExamQuestion eq JOIN FETCH eq.question WHERE eq.exam.id = :examId")
    List<ExamQuestion> findByExamIdWithQuestions(@Param("examId") Long examId);

    List<ExamQuestion> findExamQuestionByExam_Id(Long examId);

    ExamQuestion findQuestionByExam_IdAndQuestion_Id(Long examId, Long questionId);
}
