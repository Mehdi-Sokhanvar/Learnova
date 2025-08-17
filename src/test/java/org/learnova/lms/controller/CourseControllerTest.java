package org.learnova.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Role;
import org.learnova.lms.domain.user.Student;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.request.CourseRequestDTO;
import org.learnova.lms.dto.request.EnrollmentRoleForUser;
import org.learnova.lms.dto.request.LoginDTO;
import org.learnova.lms.exception.StudnetNotAssignedThisCourse;
import org.learnova.lms.exception.TeacherNotAssignedThisCourse;
import org.learnova.lms.repository.course.CourseRepository;
import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.StudentRepository;
import org.learnova.lms.repository.user.TeacherRepository;
import org.learnova.lms.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseControllerTest {

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


    private Faker faker = new Faker();
    private String adminAccessToken;

    @BeforeEach
    void setUp() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUserName("admin@email");
        appUser.setEmail("admin@email");
        appUser.setRole(new Role("ADMIN"));
        appUser.setPassword(passwordEncoder.encode("123456789"));
        AppUser userSaved = userRepository.save(appUser);
        LoginDTO adminLogin = new LoginDTO(userSaved.getUserName(), "123456789");
        String contentAsString = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(adminLogin))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        this.adminAccessToken = objectMapper.readTree(contentAsString).get("access_token").asText();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void addCourse() throws Exception {
        CourseRequestDTO requestDTO = new CourseRequestDTO(faker.educator().course(), faker.team().name(), "2023-12-03", "2025-12-23");
        mockMvc.perform(
                post("/api/v1/courses")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().isCreated());
    }


    @Test
    void occurs_DataTimeException() throws Exception {
        CourseRequestDTO requestDTO = new CourseRequestDTO(faker.educator().course(), faker.team().name(), "2023-12-03", "2022-12-23");
        mockMvc.perform(
                post("/api/v1/courses")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().isBadRequest());

    }

    @Test
    void createCourse_invalidInput_shouldReturn400() throws Exception {
        List<CourseRequestDTO> invalidRequests = List.of(
                new CourseRequestDTO("", "description", "2025-08-01", "2025-08-10"),
                new CourseRequestDTO("Course Name", "description", "", "2025-08-10"),
                new CourseRequestDTO("Course Name", "description", "2025-08-01", ""),
                new CourseRequestDTO("", "description", "", "")
        );

        for (CourseRequestDTO dto : invalidRequests) {
            mockMvc.perform(post("/api/v1/courses")
                            .header("Authorization", "Bearer " + adminAccessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void assignRole() throws Exception {
        Course courseCreated = courseRepository.save(new Course(faker.educator().course(), faker.educator().course(), LocalDate.parse("2025-08-01"), LocalDate.parse("2025-08-10"), UUID.randomUUID()));

        Teacher teacherSaved = teacherRepository.save(new Teacher("teacher@gmail.com", passwordEncoder.encode("123456789"), "teacher@gmail.com", new Role("TEACHER")));
        Student studentSaved = studentRepository.save(new Student("student@gmail.com", passwordEncoder.encode("123456789"), "student@gmail.com", new Role("STUDENT")));

        mockMvc.perform(
                post("/api/v1/courses/assign-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .content(objectMapper.writeValueAsString(new EnrollmentRoleForUser(teacherSaved.getId(), courseCreated.getId())))
        ).andExpect(status().isOk());


        mockMvc.perform(
                post("/api/v1/courses/assign-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .content(objectMapper.writeValueAsString(new EnrollmentRoleForUser(studentSaved.getId(), courseCreated.getId())))
        ).andExpect(status().isOk());

        studentRepository.deleteById(studentSaved.getId());
        teacherRepository.deleteById(teacherSaved.getId());
    }

    @Test
    void editCourse() throws Exception {

        Course courseCreated = courseRepository.save(new Course(faker.educator().course(), faker.educator().course(), LocalDate.parse("2025-08-01"), LocalDate.parse("2025-08-10"), UUID.randomUUID()));

        mockMvc.perform(
                put("/api/v1/courses/" + courseCreated.getId())
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CourseRequestDTO(faker.educator().course(), faker.educator().course(), "2025-08-01", "2025-08-10")))
        ).andExpect(status().isOk());

    }

    @Test
    void deleteCourse() throws Exception {
        Course courseCreated = courseRepository.save(new Course(faker.educator().course(), faker.educator().course(), LocalDate.parse("2025-08-01"), LocalDate.parse("2025-08-10"), UUID.randomUUID()));
        mockMvc.perform(
                delete("/api/v1/courses/" + courseCreated.getId())
                        .header("Authorization", "Bearer " + adminAccessToken)
        ).andExpect(status().isOk());
    }


    @Test
    void updateCourse_courseNotFound_shouldReturn404() throws Exception {
        CourseRequestDTO dto = new CourseRequestDTO("Java Course", "desc", "2025-08-01", "2025-08-10");
        mockMvc.perform(put("/api/v1/courses/9999")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCourse_courseNotFound_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/v1/courses/9999")
                        .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().isBadRequest());

    }

    @Test
    void assignRole_userNotFound_shouldReturn404() throws Exception {
        Course course = courseRepository.save(new Course("Course", "desc", LocalDate.now(), LocalDate.now().plusDays(5), UUID.randomUUID()));
        EnrollmentRoleForUser dto = new EnrollmentRoleForUser(9999L, course.getId());

        mockMvc.perform(post("/api/v1/courses/assign-role")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserFromCourse_userNotEnrolled_shouldReturn400() throws Exception {
        Course course = courseRepository.save(new Course("Course", "desc", LocalDate.now(), LocalDate.now().plusDays(5), UUID.randomUUID()));
        Student student = studentRepository.save(new Student("stud@gmail.com", passwordEncoder.encode("123456789"), "stud@gmail.com", new Role("STUDENT")));

        mockMvc.perform(delete("/api/v1/courses/" + student.getId() + "/course/" + course.getId())
                        .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().isBadRequest());
    }






}