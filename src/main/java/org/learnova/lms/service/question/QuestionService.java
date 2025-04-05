package org.learnova.lms.service.question;

import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.QuestionResponseDTO;
import org.learnova.lms.dto.request.QuestionDTO;
import org.learnova.lms.dto.request.QuestionRequestDto;

import java.util.List;

public interface QuestionService {
    void addQuestion(AppUser teacher, QuestionDTO dto);

//    List<QuestionDTO> findAllQuestionsCourse(AppUser teacher, Long courseId);

    void   addQuestionToExam(Teacher teacher,QuestionRequestDto requestDto);

    void deleteQuestion(Teacher teacher, Long questionId);

    void deleteQuestionFromExam(Teacher teacher, Long questionId, Long examId);

    List<QuestionResponseDTO> findAllExamQuestions(Teacher teacher, Long examId);

    List<QuestionResponseDTO> findTeacherQuestion(Teacher teacher);
}
