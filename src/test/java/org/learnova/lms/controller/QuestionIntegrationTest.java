package org.learnova.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.enums.QType;
import org.learnova.lms.domain.enums.QuestionLevel;
import org.learnova.lms.domain.question.Category;
import org.learnova.lms.domain.question.type_question.EssayQuestion;
import org.learnova.lms.domain.question.type_question.MultipleOptionQuestion;
import org.learnova.lms.domain.question.type_question.TrueFalseQuestion;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Role;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.request.EssayRequestDTO;
import org.learnova.lms.dto.request.LoginDTO;
import org.learnova.lms.dto.request.QuestionDTO;
import org.learnova.lms.repository.course.CourseRepository;
import org.learnova.lms.repository.question.QuestionRepository;
import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuestionIntegrationTest {


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
    private QuestionRepository questionRepository;

    private Faker faker = new Faker();
    private String teacherAccessToken;
    private Course course;


    @BeforeEach
    void setUp() throws Exception {

        Teacher teacher =
                new Teacher("teacher@gmail.com",
                        passwordEncoder.encode("123456789"), "teacher@gmail.com",
                        new Role("TEACHER"));

        userRepository.save(teacher);

        LoginDTO adminLogin = new LoginDTO("teacher@gmail.com", "123456789");
        String contentAsString = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(adminLogin))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        this.teacherAccessToken = objectMapper.readTree(contentAsString).get("access_token").asText();

        Course course = new Course(faker.educator().course(), faker.educator().course(), LocalDate.parse("2023-12-03"), LocalDate.parse("2025-12-23"), UUID.randomUUID());
        course.setTeacher(teacher);
        this.course = courseRepository.save(course);
    }


    @AfterEach
    void afterEach() {
        questionRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void givenValidEssayRequest_whenCreateQuestion_thenReturns201Created() throws Exception {
        String content = String.format(
                """
                        {
                          "type": "ESSAY",
                          "dtype": "ESSAY",
                          "title": "this is edit Explain OOP concepts",
                          "description": "asdkhjfd21312wkdshflkhalhlkhlkasdhflaksdhflaksdjhasdlkfjahsdf",
                          "identifier": "Q12345111112s",
                          "defaultScore": 10.0,
                          "level": "SIMPLE",
                          "category": "Fizik",
                          "courseId": %d,
                          "answer": "OOP is a programming paradigm based on objects...",
                          "maxLength": 500
                        }
                        
                        """, this.course.getId());

        mockMvc.perform(
                post("/api/v1/questions")
                        .header("Authorization", "Bearer " + this.teacherAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)

        ).andExpect(status().isCreated());
    }


    @Test
    void  givenValidTrueFalseRequest_whenCreateQuestion_thenReturns201Created() throws Exception {
        String content = String.format("""
                {
                  "type": "TRUE_FALSE",
                  "dtype": "TRUE_FALSE",
                  "title": "edit.",
                  "description": "Is this statement true or false?",
                  "identifier": "Q1011122222121211201111s31ss",
                  "defaultScore": 2.0,
                  "level": "SIMPLE",
                  "category": "Fizik",
                  "courseId": %d,
                  "isCorrect": false
                }
                
                """, this.course.getId());

        mockMvc.perform(
                post("/api/v1/questions")
                        .header("Authorization", "Bearer " + this.teacherAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)

        ).andExpect(status().isCreated());
    }

    @Test
    void givenValidMultipleChoiceRequest_whenCreateQuestion_thenReturns201Created()  throws Exception {
        String content = String.format("""
                {
                  "type": "MULTIPLE_CHOICE",
                  "dtype": "MULTIPLE_CHOICE",
                  "title": "What is JAVA?",
                  "description": "Select the correct definition of Java.",
                  "defaultScore": 5.0,
                  "level": "MEDIUM",
                  "category": "Math",
                  "courseId": %d,
                  "options": [
                    { "text": "A programming language", "isCorrect": true },
                    { "text": "A coffee brand", "isCorrect": false },
                    { "text": "A web browser", "isCorrect": false }
                  ],
                  "shuffled": true
                }
                """, this.course.getId());

        mockMvc.perform(
                post("/api/v1/questions")
                        .header("Authorization", "Bearer " + this.teacherAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)

        ).andExpect(status().isCreated());
    }
    @Test
    void givenExistingEssayQuestion_whenDeleteQuestion_thenReturns200Ok()   throws Exception {
        EssayQuestion question = new EssayQuestion();
        question.setTitle(faker.educator().course());
        question.setDescription(faker.educator().course());
        question.setDefaultScore(12.12);
        question.setIdentifier(UUID.randomUUID().toString());
        question.setLevel(QuestionLevel.SIMPLE);
        question.setCategory(new Category("Math"));
        question.setAnswer("OOP is a programming language");
        question.setMaxLength(12);
        question.setTeacher(userRepository.findByUserName("teacher@gmail.com"));
        question.setType(QType.ESSAY);
        EssayQuestion questionSaved = questionRepository.save(question);
        mockMvc.perform(
                delete("/api/v1/questions/" + questionSaved.getId())
                .header("Authorization", "Bearer " + this.teacherAccessToken)
        ).andExpect(status().isOk());

    }


    @Test
    void givenExistingTrueFalseQuestion_whenDeleteQuestion_thenReturns200Ok()   throws Exception {
        TrueFalseQuestion question = new TrueFalseQuestion();
        question.setTitle(faker.educator().course());
        question.setDescription(faker.educator().course());
        question.setDefaultScore(12.12);
        question.setIdentifier(UUID.randomUUID().toString());
        question.setLevel(QuestionLevel.SIMPLE);
        question.setCategory(new Category("Math"));
        question.setTeacher(userRepository.findByUserName("teacher@gmail.com"));
        question.setType(QType.ESSAY);
        TrueFalseQuestion questionSaved = questionRepository.save(question);
        mockMvc.perform(
                delete("/api/v1/questions/" + questionSaved.getId())
                        .header("Authorization", "Bearer " + this.teacherAccessToken)
        ).andExpect(status().isOk());
    }


    @Test
    void givenExistingMultipleChoiceQuestion_whenDeleteQuestion_thenReturns200Ok()   throws Exception {
        MultipleOptionQuestion question = new MultipleOptionQuestion();
        question.setTitle(faker.educator().course());
        question.setDescription(faker.educator().course());
        question.setDefaultScore(12.12);
        question.setIdentifier(UUID.randomUUID().toString());
        question.setLevel(QuestionLevel.SIMPLE);
        question.setCategory(new Category("Math"));
        question.setTeacher(userRepository.findByUserName("teacher@gmail.com"));
        question.setType(QType.ESSAY);
        MultipleOptionQuestion questionSaved = questionRepository.save(question);
        mockMvc.perform(
                delete("/api/v1/questions/" + questionSaved.getId())
                        .header("Authorization", "Bearer " + this.teacherAccessToken)
        ).andExpect(status().isOk());
    }



}
