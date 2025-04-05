package org.learnova.lms.service.exam;

import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.*;
import org.learnova.lms.dto.request.ExamRequestDTO;
import org.learnova.lms.dto.response.ExamResponseDTO;

import java.util.List;
import java.util.Locale;


public interface ExamService {

    void addExam(Teacher user, ExamRequestDTO exam);

    void updateExam(AppUser user, Long id, ExamRequestDTO exam);

    void deleteExam(Long examId,Long userId);

    List<ExamResponseDTO> examListInCourse(Long courseId,AppUser user);


    ExamSessionResponseDTO startExam(Long examId, Long id, Locale locate);

    QuestionResponseDTO getCurrentQuestion(Long sessionId, AppUser studentInExam, Locale locale);

    void changeCurrentQuestion(Long sessionId, String direction, AppUser studentInExam, Locale locale);

    void saveAnswerStudent(Long sessionId, AnswerDTO answer, AppUser studentInExam, Locale locale);

    List<AllAnswersQuestionDTO> getAllAnswerQuestionStudent(Long sessionId, AppUser studentInExam,Locale locale);

    void submitSessionExam(Long sessionId, AppUser studentInExam,Locale locale);

    TeacherReportAsStudent getAllDetailReportAsStudent(Long examId, Long studentId, AppUser teacher, Locale locale);

    ExamSessionResponseDTO continueExam(Long examId, AppUser student, Locale locale);

    void assignScoreToEssayQuestion(Long examId, assignScoreDTO requestDTO,Locale locale);

    List<ReportDTO> getReport(Long examId, AppUser teacher);
}
