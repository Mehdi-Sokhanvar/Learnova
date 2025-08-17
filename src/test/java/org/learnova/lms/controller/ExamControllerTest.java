package org.learnova.lms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.enums.ExamStatus;
import org.learnova.lms.domain.enums.QType;
import org.learnova.lms.domain.enums.QuestionLevel;
import org.learnova.lms.domain.exam.Exam;
import org.learnova.lms.domain.exam.ExamQuestion;
import org.learnova.lms.domain.exam.ExamSession;
import org.learnova.lms.domain.exam.StudentAnswer;
import org.learnova.lms.domain.question.Category;
import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.question.type_question.EssayQuestion;
import org.learnova.lms.domain.question.type_question.MultipleOptionQuestion;
import org.learnova.lms.domain.question.type_question.TrueFalseQuestion;
import org.learnova.lms.domain.user.Role;
import org.learnova.lms.domain.user.Student;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.request.ExamRequestDTO;
import org.learnova.lms.dto.request.LoginDTO;
import org.learnova.lms.repository.ExamSessionRepository;
import org.learnova.lms.repository.course.CourseRepository;
import org.learnova.lms.repository.exam.ExamRepository;
import org.learnova.lms.repository.question.ExamQuestionRepository;
import org.learnova.lms.repository.question.QuestionRepository;
import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.StudentRepository;
import org.learnova.lms.repository.user.TeacherRepository;
import org.learnova.lms.repository.user.UserRepository;
import org.learnova.lms.service.exam.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExamControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamSessionRepository examSessionRepository;
    @Autowired
    private ExamQuestionRepository examQuestionRepository;


    private String teacherAccessToken;
    private Course course;
    private Faker faker = new Faker();
    private String studentAccessToken;

    @BeforeEach
    void setUp() throws Exception {

        Teacher teacher =
                new Teacher("teacher@gmail.com",
                        passwordEncoder.encode("123456789"), "teacher@gmail.com",
                        new Role("TEACHER"));

        Student student = new Student("student@gmail.com",
                passwordEncoder.encode("123456789"), "student@gmail.com",
                new Role("STUDENT"));

        userRepository.save(teacher);
        userRepository.save(student);

        LoginDTO teacherLogin = new LoginDTO("teacher@gmail.com", "123456789");
        String teacherToken = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(teacherLogin))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        this.teacherAccessToken = objectMapper.readTree(teacherToken).get("access_token").asText();


        LoginDTO studentLogin = new LoginDTO("student@gmail.com", "123456789");
        String studentToken = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(studentLogin))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        this.studentAccessToken = objectMapper.readTree(studentToken).get("access_token").asText();

        Course course = new Course(faker.educator().course(), faker.educator().course(),
                LocalDate.parse("2021-12-03"), LocalDate.parse("2025-12-23"), UUID.randomUUID());
        course.setTeacher(teacher);
        course.setStudentList(List.of(student));
        this.course = courseRepository.save(course);
    }


    @AfterEach
    void afterEach() {
        examSessionRepository.deleteAll();
        examQuestionRepository.deleteAll();
        examRepository.deleteAll();
        courseRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addExam() throws Exception {
        ExamRequestDTO examRequestDTO
                = new ExamRequestDTO("title",
                "description",
                "2022-12-12",
                LocalDateTime.parse("2024-03-21T09:30:00"),
                LocalDateTime.parse("2024-03-21T11:30:00"),
                course.getId(),
                "Asia/Tehran",
                70.0,
                56.0);
        mockMvc.perform(
                post("/api/v1/exam")
                        .header("Authorization", "Bearer " + teacherAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examRequestDTO))
        ).andExpect(status().isCreated());
    }

    @Test
    void editExam() throws Exception {
        ZoneId examZone = ZoneId.of("Asia/Tehran");

        Instant startTime = LocalDate.parse("2022-12-12")
                .atTime(9, 30)                  // ساعت دلخواه
                .atZone(examZone)
                .toInstant();

        Instant endTime = LocalDate.parse("2022-12-12")
                .atTime(11, 30)                 // ساعت پایان
                .atZone(examZone)
                .toInstant();
        Exam saveExam = examRepository.save(
                new Exam(
                        "title",
                        "description",
                        LocalDate.parse("2022-12-12"),
                        startTime,
                        endTime,
                        String.valueOf(examZone),
                        ExamStatus.InProgress,
                        courseRepository.findById(this.course.getId()).get(),
                        teacherRepository.findTeacherByEmail("teacher@gmail.com").get(),
                        70.0,
                        56.0));


        ExamRequestDTO examRequestDTO
                = new ExamRequestDTO("title",
                "description",
                "2022-12-12",
                LocalDateTime.parse("2024-03-21T09:30:00"),
                LocalDateTime.parse("2024-03-21T11:30:00"),
                course.getId(),
                "Asia/Tehran",
                70.0,
                56.0);

        mockMvc.perform(
                put("/api/v1/exam/" + saveExam.getId())
                        .header("Authorization", "Bearer " + teacherAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examRequestDTO))
        ).andExpect(status().isOk());
    }

    @Test
    void deleteExam() throws Exception {
        ZoneId examZone = ZoneId.of("Asia/Tehran");

        Instant startTime = LocalDate.parse("2022-12-12")
                .atTime(9, 30)                  // ساعت دلخواه
                .atZone(examZone)
                .toInstant();

        Instant endTime = LocalDate.parse("2022-12-12")
                .atTime(11, 30)                 // ساعت پایان
                .atZone(examZone)
                .toInstant();
        Exam saveExam = examRepository.save(
                new Exam(
                        "title",
                        "description",
                        LocalDate.parse("2022-12-12"),
                        startTime,
                        endTime,
                        String.valueOf(examZone),
                        ExamStatus.InProgress,
                        courseRepository.findById(this.course.getId()).get(),
                        teacherRepository.findTeacherByEmail("teacher@gmail.com").get(),
                        70.0,
                        56.0));


        mockMvc.perform(
                delete("/api/v1/exam/" + saveExam.getId())
                        .header("Authorization", "Bearer " + teacherAccessToken)
        ).andExpect(status().isOk());
    }

    @Test
    void getAllExams() throws Exception {
        ZoneId examZone = ZoneId.of("Asia/Tehran");

        Instant startTime = LocalDate.parse("2022-12-12")
                .atTime(9, 30)                  // ساعت دلخواه
                .atZone(examZone)
                .toInstant();

        Instant endTime = LocalDate.parse("2022-12-12")
                .atTime(11, 30)                 // ساعت پایان
                .atZone(examZone)
                .toInstant();
        Exam saveExam0 = examRepository.save(
                new Exam(
                        "title",
                        "description",
                        LocalDate.parse("2022-12-12"),
                        startTime,
                        endTime,
                        String.valueOf(examZone),
                        ExamStatus.InProgress,
                        courseRepository.findById(this.course.getId()).get(),
                        teacherRepository.findTeacherByEmail("teacher@gmail.com").get(),
                        70.0,
                        56.0));
        Exam saveExam1 = examRepository.save(
                new Exam(
                        "title",
                        "description",
                        LocalDate.parse("2022-12-12"),
                        startTime,
                        endTime,
                        String.valueOf(examZone),
                        ExamStatus.InProgress,
                        courseRepository.findById(this.course.getId()).get(),
                        teacherRepository.findTeacherByEmail("teacher@gmail.com").get(),
                        70.0,
                        56.0));

        mockMvc.perform(
                        get("/api/v1/exam/" + this.course.getId() + "/all")
                                .header("Authorization", "Bearer " + teacherAccessToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].date").value("2022-12-12"))
                .andExpect(jsonPath("$[0].courseName").value(this.course.getTitle()))
                .andExpect(jsonPath("$[1].title").value("title"))
                .andExpect(jsonPath("$[1].description").value("description"));


    }

    @Test
    void startExam() throws Exception {
        ZoneId examZone = ZoneId.of("Asia/Tehran");

        Instant startTime = LocalDate.parse("2022-12-12")
                .atTime(9, 30)                  // ساعت دلخواه
                .atZone(examZone)
                .toInstant();

        Instant endTime = LocalDate.parse("2026-12-12")
                .atTime(11, 30)                 // ساعت پایان
                .atZone(examZone)
                .toInstant();
        Exam saveExam = examRepository.save(
                new Exam(
                        "title",
                        "description",
                        LocalDate.parse("2022-12-12"),
                        startTime,
                        endTime,
                        String.valueOf(examZone),
                        ExamStatus.InProgress,
                        courseRepository.findById(this.course.getId()).get(),
                        teacherRepository.findTeacherByEmail("teacher@gmail.com").get(),
                        70.0,
                        56.0));


        mockMvc.perform(
                get("/api/v1/exam/" + saveExam.getId() + "/start")
                        .header("Authorization", "Bearer " + studentAccessToken)
        ).andExpect(status().isCreated());
    }

    @Test
    void getCurrentQuestion() throws Exception {

        MultipleOptionQuestion question1 = new MultipleOptionQuestion();
        question1.setTitle(faker.educator().course());
        question1.setDescription(faker.educator().course());
        question1.setDefaultScore(12.12);
        question1.setIdentifier(UUID.randomUUID().toString());
        question1.setLevel(QuestionLevel.SIMPLE);
        question1.setCategory(new Category("Math"));
        question1.setTeacher(userRepository.findByUserName("teacher@gmail.com"));
        question1.setType(QType.MULTIPLE_CHOICE);
        MultipleOptionQuestion questionSaved1 = questionRepository.save(question1);

        TrueFalseQuestion question2 = new TrueFalseQuestion();
        question2.setTitle(faker.educator().course());
        question2.setDescription(faker.educator().course());
        question2.setDefaultScore(12.12);
        question2.setIdentifier(UUID.randomUUID().toString());
        question2.setLevel(QuestionLevel.SIMPLE);
        question2.setCategory(new Category("Math"));
        question2.setTeacher(userRepository.findByUserName("teacher@gmail.com"));
        question2.setType(QType.TRUE_FALSE);
        TrueFalseQuestion questionSaved2 = questionRepository.save(question2);

        EssayQuestion question3 = new EssayQuestion();
        question3.setTitle(faker.educator().course());
        question3.setDescription(faker.educator().course());
        question3.setDefaultScore(12.12);
        question3.setIdentifier(UUID.randomUUID().toString());
        question3.setLevel(QuestionLevel.SIMPLE);
        question3.setCategory(new Category("Math"));
        question3.setAnswer("OOP is a programming language");
        question3.setMaxLength(12);
        question3.setTeacher(userRepository.findByUserName("teacher@gmail.com"));
        question3.setType(QType.ESSAY);
        EssayQuestion questionSaved3 = questionRepository.save(question3);

        ZoneId examZone = ZoneId.of("Asia/Tehran");

        Instant startTime = LocalDate.parse("2022-12-12")
                .atTime(9, 30)                  // ساعت دلخواه
                .atZone(examZone)
                .toInstant();

        Instant endTime = LocalDate.parse("2026-12-12")
                .atTime(11, 30)                 // ساعت پایان
                .atZone(examZone)
                .toInstant();
        Exam saveExam = examRepository.save(
                new Exam(
                        "title",
                        "description",
                        LocalDate.parse("2022-12-12"),
                        startTime,
                        endTime,
                        String.valueOf(examZone),
                        ExamStatus.InProgress,
                        courseRepository.findById(this.course.getId()).get(),
                        teacherRepository.findTeacherByEmail("teacher@gmail.com").get(),
                        70.0,
                        56.0));

        ExamQuestion examQuestion1=examQuestionRepository.save(new ExamQuestion(saveExam,questionSaved1,10.0));
        ExamQuestion examQuestion2=examQuestionRepository.save(new ExamQuestion(saveExam,questionSaved2,10.0));
        ExamQuestion examQuestion3=examQuestionRepository.save(new ExamQuestion(saveExam,questionSaved3,10.0));
        List<ExamQuestion> questions =  examQuestionRepository.findExamQuestionByExam_Id(saveExam.getId());
        List<Long> questionIds = questions.stream().map(ExamQuestion::getId).collect(Collectors.toList());
        ExamSession examSession=examSessionRepository.save(
                new ExamSession(
                        saveExam,studentRepository.findStudentByEmail("student@gmail.com").get()
                        ,Instant.now(),Instant.now(),ExamStatus.InProgress,0.0,questionIds,List.of(new StudentAnswer()),0
                )
        );

        mockMvc.perform(
                get("/api/v1/exam/exam-sessions/" + examSession.getId() + "/current")
                        .header("Authorization", "Bearer " + studentAccessToken)
        ).andExpect(status().isOk());
    }





}