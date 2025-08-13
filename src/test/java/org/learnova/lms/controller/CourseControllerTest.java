package org.learnova.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.learnova.lms.dto.request.CourseRequestDTO;
import org.learnova.lms.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

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

    private Faker faker = new Faker();

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "adZXZxXZmzin",roles = "qwqwqwqq")
    void addCourse() throws Exception {

        CourseRequestDTO requestDTO=new CourseRequestDTO(faker.educator().course(), faker.team().name(), "2023-12-03","2025-12-23");
        mockMvc.perform(
                post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andExpect(status().isCreated());
    }

//    @Test
//    void assignRole() {
//    }
//
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