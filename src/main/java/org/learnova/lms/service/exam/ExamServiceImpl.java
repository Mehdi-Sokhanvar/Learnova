package org.learnova.lms.service.exam;

import org.learnova.lms.domain.exam.Exam;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.enums.ExamStatus;
import org.learnova.lms.domain.exam.ExamQuestion;
import org.learnova.lms.domain.exam.ExamSession;
import org.learnova.lms.domain.exam.StudentAnswer;
import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.question.type_question.MultipleOptionQuestion;
import org.learnova.lms.domain.question.type_question.TrueFalseQuestion;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.*;
import org.learnova.lms.dto.request.ExamRequestDTO;
import org.learnova.lms.dto.response.ExamResponseDTO;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.exception.*;
import org.learnova.lms.repository.ExamSessionRepository;
import org.learnova.lms.repository.StudentAnswerRepository;
import org.learnova.lms.repository.course.CourseRepository;
import org.learnova.lms.repository.exam.ExamRepository;
import org.learnova.lms.repository.question.ExamQuestionRepository;
import org.learnova.lms.repository.question.QuestionRepository;
import org.learnova.lms.repository.user.StudentRepository;
import org.learnova.lms.repository.user.UserRepository;
import org.learnova.lms.service.question.QuestionServiceImpl;
import org.learnova.lms.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {


    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ExamSessionRepository examSessionRepository;
    private final StudentRepository studentRepository;
    private final ExamQuestionRepository examQuestionRepository;
    @Autowired
    private MessageSource messageSource;
    private final QuestionRepository questionRepository;
    private final QuestionServiceImpl questionServiceImpl;
    private final StudentAnswerRepository studentAnswerRepository;

    public ExamServiceImpl(ExamRepository examRepository, CourseRepository courseRepository, UserRepository userRepository, ExamSessionRepository examSessionRepository, StudentRepository studentRepository, ExamQuestionRepository examQuestionRepository, QuestionRepository questionRepository, QuestionServiceImpl questionServiceImpl, StudentAnswerRepository studentAnswerRepository) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.examSessionRepository = examSessionRepository;
        this.studentRepository = studentRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.questionRepository = questionRepository;
        this.questionServiceImpl = questionServiceImpl;
        this.studentAnswerRepository = studentAnswerRepository;
    }

    @Override
    public void addExam(Teacher teacher, ExamRequestDTO exam) {
        LocalDate examDate = LocalDate.parse(exam.date());
        Course course = courseRepository.findById(exam.courseId())
                .orElseThrow(() -> new CourseNotFoundException(
                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, exam.courseId())));
        validateTeacherAssignment(teacher, course);
        validateExamDateWithinCourse(examDate, course);
        validateTimezone(exam.examTimeZone());

        ZoneId examZone = ZoneId.of(exam.examTimeZone());
        Instant startTimeToUTC = exam.startTime().atZone(examZone).toInstant();
        Instant endTimeToUTC = exam.endTime().atZone(examZone).toInstant();

        if (endTimeToUTC.isBefore(startTimeToUTC)) {
            throw new DataExamBetweenStartDateAndEndDateException("End time must be after start time");
        }

        Exam newExam = new Exam(
                exam.title(),
                exam.description(),
                examDate,
                startTimeToUTC,
                endTimeToUTC,
                exam.examTimeZone(),
                ExamStatus.NotStarted,
                course,
                teacher,
                exam.mark(),
                exam.passiveMark()
        );

        examRepository.save(newExam);
//        courseRepository.findById(exam.courseId()).ifPresentOrElse(
//                course -> {
//                    if (course.getTeacher().getUserName().equals(user.getUserName())) {
//                        if (LocalDate.parse(exam.date()).isAfter(course.getStartDate()) &&
//                                LocalDate.parse(exam.date()).isBefore(course.getEndDate())) {
//                            examRepository.save(
//                                    new Exam(
//                                            exam.title(),
//                                            exam.description(),
//                                            LocalDate.parse(exam.date()),
//                                            LocalTime.parse(exam.startTime()),
//                                            exam.duration(),
//                                            ExamStatus.NotStarted,
//                                            course));
//                        } else {
//                            throw new DataExamBetweenStartDateAndEndDateException(Messages.DATE_EXAM_BETWEEN_START_DATE_AND_END_DATE);
//                        }
//
//                    } else {
//                        throw new TeacherNotAssignedThisCourse(String.format(Messages.TEACHER_NOT_ASSIGNED_IN_THIS_COURSE, user.getId()));
//                    }
//                },
//                () -> {
//                    throw new CourseNotFoundException(String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, exam.courseId()));
//                }
//        );
    }


    @Override
    public void updateExam(AppUser user, Long id, ExamRequestDTO exam) {
        LocalDate examDate = LocalDate.parse(exam.date());
        Course course = courseRepository.findById(exam.courseId())
                .orElseThrow(() -> new CourseNotFoundException(
                        String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, exam.courseId())));

        Exam existingExam = examRepository.findById(id)
                .orElseThrow(() -> new ExamNotFoundException(
                        String.format(Messages.EXAM_NOT_FOUND_BY_THIS_ID, id)));

        validateTeacherAssignment(user, course);

        ZoneId examZone = ZoneId.of(exam.examTimeZone());
        Instant startTimeToUTC = exam.startTime().atZone(examZone).toInstant();
        Instant endTimeToUTC = exam.endTime().atZone(examZone).toInstant();


        existingExam.setTitle(exam.title());
        existingExam.setDate(examDate);
        existingExam.setStartTime(startTimeToUTC);
        existingExam.setEndTime(endTimeToUTC);
        existingExam.setExamTimeZone(exam.examTimeZone());

        examRepository.save(existingExam);
    }

    @Override
    public void deleteExam(Long id, Long teacherId) {
        Exam examFound = examRepository.findById(id)
                .orElseThrow(() -> new ExamNotFoundException(String.format(Messages.EXAM_NOT_FOUND_BY_THIS_ID, id)));
        if (examFound.getCourse().getTeacher().getId() != teacherId) {
            throw new TeacherNotAssignedThisCourse(String.format(Messages.TEACHER_NOT_ASSIGNED_IN_THIS_COURSE, teacherId));
        }
        examRepository.delete(examFound);

    }


    @Override
    public List<ExamResponseDTO> examListInCourse(Long courseId, AppUser user) {
        Course courseFound = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new CourseNotFoundException(String.format(Messages.COURSE_NOT_FOUND_BY_THIS_ID, courseId)));

        if (user.getRole().getName().equals("ROLE_TEACHER")) {
            if (!courseFound.getTeacher().getId().equals(user.getId())) {
                throw new TeacherNotAssignedThisCourse(String.format(Messages.TEACHER_NOT_ASSIGNED_IN_THIS_COURSE, user.getId()));
            }
        } else if (user.getRole().getName().equals("ROLE_STUDENT")) {
            if (courseFound.getStudentList().contains(user)) {
                throw new StudnetNotAssignedThisCourse("studentNotAssignedThisCourse");
            }
        }

        List<Exam> examFound = examRepository.findExamByCourse(courseId);
        return examFound
                .stream()
                .map(item -> new ExamResponseDTO(
                        item.getTitle(),
                        item.getDescription(),
                        item.getDate(),
                        item.getStartTime(),
                        item.getEndTime(),
                        item.getCourse().getTitle()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ExamSessionResponseDTO startExam(Long examId, Long studentId, Locale locale) {

        Exam exam = examRepository.findById(examId).orElseThrow(() ->
                new ExamNotFoundException(String.format(Messages.EXAM_NOT_FOUND_BY_THIS_ID, examId)));

        Instant now = Instant.now();
        ZoneId examZone = ZoneId.of(exam.getExamTimeZone());
        Instant startTimeToUTC = now.atZone(examZone).toInstant();
        if (startTimeToUTC.isBefore(exam.getStartTime()) || startTimeToUTC.isAfter(exam.getEndTime())) {
            throw new ExamNotStartedException(messageSource.getMessage("Exam.err", null, locale));
        }

        Optional<ExamSession> existingSession =
                examSessionRepository.findByExam_IdAndStudent_IdAndStatus(examId, studentId, ExamStatus.InProgress);
        existingSession.ifPresent(examSession -> new ExamSessionResponseDTO(
                studentId,
                examId,
                now,
                examSession.getCurrentQuestion(),
                messageSource.getMessage("ExamSession.Continue", null, locale)
        ));

        ExamSession examSession = new ExamSession();
        examSession.setStartTime(now);
        examSession.setExam(exam);
        examSession.setStudent(studentRepository.findById(studentId).orElseThrow(() ->
                new UserNotFoundException(String.format(Messages.USER_NOT_FOUND_BY_THIS_ID, studentId))));
        examSession.setStatus(ExamStatus.InProgress);

        List<ExamQuestion> questions = examQuestionRepository.findExamQuestionByExam_Id(examId);

        List<Long> questionIds = questions.stream().map(ExamQuestion::getId).collect(Collectors.toList());

        examSession.setQuestionOrder(questionIds);
        examSession.setCurrentQuestion(0);
        ExamSession session = examSessionRepository.save(examSession);

        return new ExamSessionResponseDTO(
                studentId,
                examId,
                now,
                session.getCurrentQuestion(),
                messageSource.getMessage("ExamSession.New", null, locale)
        );
    }

    @Override
    public ExamSessionResponseDTO continueExam(Long examId, AppUser student, Locale locale) {
        ExamSession examSession = examSessionRepository.findByExam_IdAndStudent_Id(examId, student.getId())
                .orElseThrow(() -> new ResourceAccessException(messageSource.getMessage("Session.err", null, locale)));

        return new ExamSessionResponseDTO(
                examSession.getId(),
                examSession.getExam().getId(),
                examSession.getStartTime(),
                examSession.getCurrentQuestion(),
                messageSource.getMessage("ExamSession.Continue", null, locale)
        );
    }

    @Override
    public void assignScoreToEssayQuestion(Long examId, assignScoreDTO requestDTO, Locale locale) {
        ExamSession examSession = examSessionRepository.findByExam_IdAndStudent_Id(examId, requestDTO.StudentId())
                .orElseThrow(() -> new ResourceAccessException(messageSource.getMessage("Session.err", null, locale)));

        ExamQuestion examQuestion = examQuestionRepository.findById(requestDTO.questionId())
                .filter(q -> q.getExam().getId().equals(examId))
                .orElseThrow(() -> new QuestionNotFoundException(
                        messageSource.getMessage("question.not_found", null, locale)));

        StudentAnswer answer = studentAnswerRepository.findStudentAnswerByExamSession_IdAndExamQuestion_Id(examSession.getId(),
                        examQuestion.getId())
                .orElseThrow(() -> new ResourceAccessException(messageSource.getMessage("Session.err", null, locale)));

        if (requestDTO.score() > answer.getExamQuestion().getScore()) {
            throw new ScoreCanNotBiggerThanScoreException(messageSource.getMessage("score.err", null, locale));
        }
        answer.setScore(requestDTO.score());
        examSession.setTotalScore(examSession.getTotalScore() + requestDTO.score());
        examSessionRepository.save(examSession);
        studentAnswerRepository.save(answer);
    }

    @Override
    public List<ReportDTO> getReport(Long examId, AppUser teacher) {
        return List.of();
    }

    @Override
    public QuestionResponseDTO getCurrentQuestion(Long sessionId, AppUser studentInExam, Locale locale) {
        ExamSession examSession = validateExamSession(sessionId, studentInExam, locale);

        ExamQuestion questionFound = examQuestionRepository
                .findQuestionByExam_IdAndQuestion_Id(examSession.getExam().getId(),
                        examSession.getQuestionOrder().get(examSession.getCurrentQuestion()));

        questionFound.getQuestion().setDefaultScore(questionFound.getScore());
        return questionServiceImpl.convertToDto(questionFound.getQuestion());
    }

    @Override
    public void changeCurrentQuestion(Long sessionId, String direction, AppUser studentInExam, Locale locale) {
        ExamSession session = validateExamSession(sessionId, studentInExam, locale);
        switch (direction) {
            case "next":
                if (session.getCurrentQuestion() < session.getQuestionOrder().size() - 1) {
                    session.setCurrentQuestion(session.getCurrentQuestion() + 1);
                }
                break;
            case "previous":
                if (session.getCurrentQuestion() > 0) {
                    session.setCurrentQuestion(session.getCurrentQuestion() - 1);
                }
                break;
            default:
                throw new InvalidNavigationException(messageSource.getMessage("InvalidNavigationException.err", null, locale));
        }
        examSessionRepository.save(session);
    }

    @Override
    public void saveAnswerStudent(Long sessionId, AnswerDTO answer, AppUser studentInExam, Locale locale) {
        ExamSession session = validateExamSession(sessionId, studentInExam, locale);
        ExamQuestion questionAnswered =
                examQuestionRepository.findQuestionByExam_IdAndQuestion_Id(session.getExam().getId(), answer.questionId());
        StudentAnswer studentAnswer =
                studentAnswerRepository.findStudentAnswerByExamSession_IdAndExamQuestion_Id(sessionId,
                        questionAnswered.getId()).orElse(new StudentAnswer());
        studentAnswer.setExamSession(session);
        studentAnswer.setExamQuestion(questionAnswered);
        studentAnswer.setAnswer(answer.content());
        studentAnswerRepository.save(studentAnswer);
    }

    @Override
    public List<AllAnswersQuestionDTO> getAllAnswerQuestionStudent(Long examId, AppUser studentInExam, Locale locale) {
        ExamSession examSession = examSessionRepository.findByExam_IdAndStudent_Id(examId, studentInExam.getId()).orElseThrow(() ->
                new ResourceAccessException(messageSource.getMessage("NotAccess", null, locale)));

//        List<StudentAnswer> studentAnswers = examSession.getStudentAnswers();
//        List<Long> questionOrder = examSession.getQuestionOrder();
//        studentAnswers.sort(Comparator.comparingInt(a -> questionOrder.indexOf(a.getExamQuestion().getId())));
//        return studentAnswers.stream().map(answer->{
//            String answerText;
//
//            if (answer.getAnswer()==null || answer.getAnswer().trim().isEmpty()){
//                answerText="THIS QUESTION HAS ANSWER";
//            }else
//                answerText=answer.getAnswer().trim();
//
//            return new AllAnswersQuestionDTO(
//                    answer.getExamQuestion().getQuestion().getTitle(),
//                    answerText
//            );
//        }).collect(Collectors.toList());

        Map<Long, String> answeredQuestions = examSession.getStudentAnswers().stream()
                .collect(Collectors.toMap(
                        answer -> answer.getExamQuestion().getQuestion().getId(),
                        StudentAnswer::getAnswer
                ));

        return examSession.getQuestionOrder().stream()
                .map(questionId -> {
                    Question question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException("QuestionNotFound"));
                    String answer = answeredQuestions.getOrDefault(questionId, null);
                    return new AllAnswersQuestionDTO(
                            question.getId(),
                            question.getTitle(),
                            answer
                    );
                })
                .collect(Collectors.toList());

    }//todo : cheack this and write better when user has no answer for question just null return

    //.map(
//            answer ->
//    {
//        new AllAnswersQuestionDTO(
//                answer.getExamQuestion().getQuestion().getTitle(),
//                answer.getAnswer();
//    }
//                ).collect(Collectors.toList());/*?????*/
    @Override
    public void submitSessionExam(Long sessionId, AppUser studentInExam, Locale locale) {
        ExamSession session = validateExamSession(sessionId, studentInExam, locale);
        session.setStatus(ExamStatus.Completed);
        session.setEndTime(Instant.now());
        calculateScoreExam(session, studentInExam);
        examSessionRepository.save(session);
    }

    @Override
    public TeacherReportAsStudent getAllDetailReportAsStudent(Long examId, Long studentId, AppUser teacher, Locale locale) {
        ExamSession examSession = examSessionRepository.findByExam_IdAndStudent_Id(examId, studentId)
                .orElseThrow(() -> new ResourceAccessException(messageSource.getMessage("Session.err", null, locale)));


        Map<Long, String> answeredQuestions = examSession.getStudentAnswers().stream()
                .collect(Collectors.toMap(
                        answer -> answer.getExamQuestion().getQuestion().getId(),
                        StudentAnswer::getAnswer
                ));

        List<AllAnswersQuestionDTO> questionDTOS = examSession.getQuestionOrder().stream()
                .map(questionId -> {
                    Question question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException("QuestionNotFound"));
                    String answer = answeredQuestions.getOrDefault(questionId, null);
                    return new AllAnswersQuestionDTO(
                            question.getId(),
                            question.getTitle(),
                            answer
                    );
                })
                .collect(Collectors.toList());


        return new TeacherReportAsStudent(
                examSession.getStudent().getId(),
                examSession.getStudent().getUserName(),
                examSession.getTotalScore(),
                examSession.getStartTime(),
                examSession.getEndTime(),
                examSession.getStatus(),
                questionDTOS
        );
    }


    private void calculateScoreExam(ExamSession session, AppUser studentInExam) {
        session.getStudentAnswers().forEach(studentAnswer -> {
            ExamQuestion examQuestion = studentAnswer.getExamQuestion();
            Question question = examQuestion.getQuestion();
            if (question instanceof MultipleOptionQuestion) {
                handelingMultipleChoice(studentAnswer, (MultipleOptionQuestion) question, examQuestion);
            } else if (question instanceof TrueFalseQuestion) {
                handleTrueFalseScoring(studentAnswer, (TrueFalseQuestion) question, examQuestion);
            }
        });

        Double totalScore = session.getStudentAnswers().stream()
                .mapToDouble(answer -> answer.getScore() == null ? 0.0 : answer.getScore())
                .sum();

        session.setTotalScore(totalScore);
    }

    private void handleTrueFalseScoring(StudentAnswer studentAnswer, TrueFalseQuestion question, ExamQuestion examQuestion) {
        studentAnswer.setFeedback("this score by bot");
        try {
            boolean studentAnswerTfQuestion = Boolean.parseBoolean(studentAnswer.getAnswer());
            ;
            if (studentAnswerTfQuestion == question.getCorrect()) {
                studentAnswer.setScore(examQuestion.getScore());
            } else {
                studentAnswer.setScore(0.0);
            }
        } catch (Exception e) {
            studentAnswer.setScore(0.0);

        }
    }

    private void handelingMultipleChoice(StudentAnswer studentAnswer, MultipleOptionQuestion question, ExamQuestion examQuestion) {
        studentAnswer.setFeedback("this score by bot");
        if (question.getOptions().contains(studentAnswer.getAnswer())) {
            studentAnswer.setScore(examQuestion.getScore());
        } else {
            studentAnswer.setScore(0.0);
        }
    }


    private ExamSession validateExamSession(Long sessionId, AppUser studentInExam, Locale locale) {
        ExamSession examSession = examSessionRepository.findById(sessionId).orElseThrow(() ->
                new SessionNotFoundException(messageSource.getMessage("Session.err", null, locale)));
        if (!examSession.getStudent().getId().equals(studentInExam.getId())) {
            throw new UnauthorizedAccessException(messageSource.getMessage("Unauthrized.err", null, locale));
        }
        if (Instant.now().isAfter(examSession.getExam().getEndTime())) {
            throw new ExamExpiredException(messageSource.getMessage("ExamExpire.err", null, locale));
        }
        if (examSession.getStatus().equals(ExamStatus.Completed)) {
            throw new ExamAlreadyCompletedException(messageSource.getMessage("ExamExpireTime.err", null, locale));
        }
        return examSession;
    }


    public ExamSession getSession(Long sessionId) {
        return examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    private void validateTeacherAssignment(AppUser user, Course course) {
        if (!course.getTeacher().getUserName().equals(user.getUserName())) {
            throw new TeacherNotAssignedThisCourse(
                    String.format(Messages.TEACHER_NOT_ASSIGNED_IN_THIS_COURSE, user.getId()));
        }
    }

    private void validateExamDateWithinCourse(LocalDate examDate, Course course) {
        if (examDate.isBefore(course.getStartDate()) || examDate.isAfter(course.getEndDate())) {
            throw new DataExamBetweenStartDateAndEndDateException(Messages.DATE_EXAM_BETWEEN_START_DATE_AND_END_DATE);
        }
    }

    private void validateTimezone(String s) {
        if (!ZoneId.getAvailableZoneIds().contains(s)) {
            throw new DataExamBetweenStartDateAndEndDateException("Invalid time zone: " + s);
        }
    }
}
