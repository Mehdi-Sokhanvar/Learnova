package org.learnova.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Role;
import org.learnova.lms.dto.request.CourseRequestDTO;
import org.learnova.lms.dto.request.LoginDTO;
import org.learnova.lms.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private Faker faker = new Faker();

    private String adminAccsessToken;

    @BeforeEach
    void setUp() throws Exception {
//        String userName, String password, String email, Role role
//        AppUser admin = new AppUser("admin@gmail.com", passwordEncoder.encode("123456789"), "admin@gmail.com", new Role("ADMIN"));
        AppUser teacher = new AppUser("teacher@gmail.com", passwordEncoder.encode("123456789"), "teacher@gmail.com", new Role("TEACHER"));


//        userRepository.save(admin);
        userRepository.save(teacher);

        LoginDTO adminLogin = new LoginDTO("admin@gmail.com", "123456789");
        String contentAsString = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(adminLogin))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        this.adminAccsessToken=objectMapper.readTree(contentAsString).get("access_token").asText();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void addCourse() throws Exception {

        CourseRequestDTO requestDTO = new CourseRequestDTO(faker.educator().course(), faker.team().name(), "2023-12-03", "2025-12-23");
        mockMvc.perform(
                post("/api/v1/courses")
                        .header("Authorization", "Bearer " + adminAccsessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().isCreated());
    }


    @Test
    void occurs_DataTimeException() throws Exception {

        CourseRequestDTO requestDTO = new CourseRequestDTO(faker.educator().course(), faker.team().name(), "2023-12-03", "2022-12-23");

        mockMvc.perform(
                post("/api/v1/courses")
                        .header("Authorization", "Bearer " + adminAccsessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().isBadRequest());

    }

    @Test
    void createCourse_invalidInput_shouldReturn400() throws Exception {
        // ساخت لیستی از درخواست‌های نامعتبر
        List<CourseRequestDTO> invalidRequests = List.of(
                new CourseRequestDTO("", "description", "2025-08-01", "2025-08-10"), // name خالی
                new CourseRequestDTO("Course Name", "description", "", "2025-08-10"), // startDate خالی
                new CourseRequestDTO("Course Name", "description", "2025-08-01", ""), // endDate خالی
                new CourseRequestDTO("", "description", "", "") // همه خالی
        );

        for (CourseRequestDTO dto : invalidRequests) {
            mockMvc.perform(post("/api/courses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }
    }
    @Test
    void assignRole() {

    }

//    @Test
//    void editCourse() {
//    }
//
//    @Test
//    void deleteCourse() {
//    }
//
//    @Test
//    void deleteUserFromCourse() {
//    }
//
//    @Test
//    void listStudentFromCourse() {
//    }
}