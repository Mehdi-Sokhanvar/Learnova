package org.learnova.lms.service.question;


import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.enums.ExamStatus;
import org.learnova.lms.domain.exam.Exam;
import org.learnova.lms.domain.exam.ExamQuestion;
import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.request.QuestionDTO;
import org.learnova.lms.dto.request.QuestionRequestDto;
import org.learnova.lms.dto.QuestionResponseDTO;
import org.learnova.lms.exception.*;
import org.learnova.lms.repository.course.CourseRepository;
import org.learnova.lms.repository.exam.ExamRepository;
import org.learnova.lms.repository.question.ExamQuestionRepository;
import org.learnova.lms.repository.question.QuestionRepository;
import org.learnova.lms.service.question.questionFactory.QuestionFactory;
import org.learnova.lms.service.question.questionFactory.QuestionFactoryProvider;
import org.learnova.lms.util.Messages;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final QuestionFactoryProvider provider;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamRepository examRepository;

    public QuestionServiceImpl(CourseRepository courseRepository, QuestionRepository questionRepository, QuestionFactoryProvider provider, ExamQuestionRepository examQuestionRepository, ExamRepository examRepository) {
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.provider = provider;
        this.examQuestionRepository = examQuestionRepository;
        this.examRepository = examRepository;
    }

    @Override
    public void addQuestion(AppUser teacher, QuestionDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException(
                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, dto.getCourseId())));
        validateTeacherAssignment(teacher, course);
        if (dto.getId() == null) {
            QuestionFactory factory = provider.getFactory(dto.getdtype());
            Question question = factory.createQuestion(dto);
            question.setCourse(course);
            question.setTeacher((Teacher) teacher);
            questionRepository.save(question);
        } else {
            Question existingQuestion = questionRepository.findById(dto.getId()).orElseThrow(() ->
                    new CourseNotFoundException("Course Not Found"));
            QuestionFactory factory = provider.getFactory(dto.getdtype());
            factory.updateQuestion(existingQuestion, dto);
            questionRepository.save(existingQuestion);
        }
    }


//    @Override
//    public void addQuestionToExam(Teacher teacher, QuestionRequestDto requestDto) {
//        Course course = courseRepository.findById(requestDto.getCourseId())
//                .orElseThrow(() -> new CourseNotFoundException(
//                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, requestDto.getCourseId())));
//        validateTeacherAssignment(teacher, course);
//        Exam examFound = course.getExamList()
//                .stream()
//                .filter(exam -> exam.getId().equals(requestDto.getExamId()))
//                .findFirst()
//                .orElseThrow(() -> new ExamNotFoundException("Exam Not Found Exception")
//                );
//        if (examFound.getStatus() != ExamStatus.InProgress
//                || examFound.getStatus() != ExamStatus.Completed) {
//            Question questionFound = course.getQuestions()
//                    .stream()
//                    .filter(question -> question.getId().equals(requestDto.getQuestionId()))
//                    .findFirst()
//                    .orElseThrow(() -> new QuestionNotFoundException("QUESTION NOT FOUND"));
//            if (!questionFound.getDeleted()) {

//                boolean questionExists = examFound.getExamQuestions().stream()
//                        .anyMatch(examQuestion -> examQuestion.getQuestion().getId().equals(requestDto.getQuestionId()));
//                if (questionExists) {
//                    throw new QuestionAlreadyExistInExam("Question already exists in the exam");
//                }
//                Double currentTotalScore = examFound.getExamQuestions().stream()
//                        .mapToDouble(ExamQuestion::getScore)
//                        .sum();
//
//                Double newScore = currentTotalScore + requestDto.getScore();
//                if (newScore > examFound.getMark()) {
//                    throw new InvalidScoreException("Total exam score cannot exceed 100");
//                }
//                examQuestionRepository.save(new ExamQuestion(examFound, questionFound, requestDto.getScore()));
//            } else {
//                throw new QuestionDeletedException("Question deleted");
//            }
//        } else {
//            throw new ExamNotAddQuestionException("Cann't Add Question To exam when question in process or completes");
//        }
//
//    }

    public void addQuestionToExam(Teacher teacher, QuestionRequestDto requestDto) {
        Course course = getValidatedCourse(requestDto.getCourseId());
        validateTeacherAssignment(teacher, course);
        Exam exam = findExamInCourse(course, requestDto.getExamId());
        validateExamAcceptingQuestions(exam);
        Question question = findActiveQuestionInCourse(course, requestDto.getQuestionId());
        validateQuestionNotInExam(exam, question.getId());
        validateScoreAddition(exam, requestDto.getScore());
        createExamQuestionEntry(exam, question, requestDto.getScore());
    }

    private Course getValidatedCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
    }

    private Exam findExamInCourse(Course course, Long examId) {
        return course.getExamList().stream()
                .filter(exam -> exam.getId().equals(examId))
                .findFirst()
                .orElseThrow(() -> new ExamNotFoundException("Exam Not Found"));
    }

    private void validateExamAcceptingQuestions(Exam exam) {
        if (exam.getStatus() == ExamStatus.InProgress || exam.getStatus() == ExamStatus.Completed) {
            throw new ExamClosedException("Exam Closed");
        }
    }

    private Question findActiveQuestionInCourse(Course course, Long questionId) {
        Question question = course.getQuestions().stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new QuestionNotFoundException("Question Not Found"));

        if (question.getDeleted()) {
            throw new QuestionDeletedException("question deleted");
        }
        return question;
    }

    private void validateQuestionNotInExam(Exam exam, Long questionId) {
        boolean exists = exam.getExamQuestions().stream()
                .anyMatch(eq -> eq.getQuestion().getId().equals(questionId));

        if (exists) {
            throw new QuestionAlreadyExistInExam("Question already exist in exam");
        }
    }

    private void validateScoreAddition(Exam exam, Double additionalScore) {
        double currentTotal = exam.getExamQuestions().stream()
                .mapToDouble(ExamQuestion::getScore)
                .sum();

        if (currentTotal + additionalScore > exam.getMark()) {
            throw new InvalidScoreException("Invalid score");
        }
    }

    private void validateTeacherAssignment(AppUser user, Course course) {
        if (!course.getTeacher().getUserName().equals(user.getUserName())) {
            throw new TeacherNotAssignedThisCourse(
                    String.format(Messages.TEACHER_NOT_ASSIGNED_IN_THIS_COURSE, user.getId()));
        }
    }

    private void createExamQuestionEntry(Exam exam, Question question, Double score) {
        examQuestionRepository.save(new ExamQuestion(exam, question, score));
    }

    @Override
    public void deleteQuestion(Teacher teacher, Long questionId) {
        Question questionFound = questionRepository.findById(questionId).orElseThrow(() ->
                new QuestionNotFoundException("QUESTION NOT FOUND"));
        if (questionFound.getTeacher().getId().equals(teacher.getId())) {
            questionFound.setDeleted(true);
            questionRepository.save(questionFound);
        }
    }

    @Override
    public void deleteQuestionFromExam(Teacher teacher, Long questionId, Long examId) {
        //todo : check teacher is teacher in course or no
        ExamQuestion courseNotFound = examQuestionRepository.findByQuestionAndExam(questionId, examId).orElseThrow(() ->
                new CourseNotFoundException("Course Not Found"));
        examQuestionRepository.delete(courseNotFound);
    }

    @Override
    public List<QuestionResponseDTO> findAllExamQuestions(Teacher teacher, Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException("Exam Not Found"));
        Course course = exam.getCourse();
        validateTeacherAssignment(teacher, course);
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamIdWithQuestions(examId);
        return examQuestions.stream()
                .map(q-> this.convertToDto(q.getQuestion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionResponseDTO> findTeacherQuestion(Teacher teacher) {
        List<Question> questions = questionRepository.findQuestionByTeacher_Id(teacher.getId());
        return questions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public QuestionResponseDTO convertToDto(Question question) {
        String questionType = String.valueOf(question.getType());
        QuestionFactory factory = provider.getFactory(questionType);
        return factory.convertToDto(question);
    }
}


