package org.learnova.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Role;
import org.learnova.lms.dto.request.RegisterDTO;
import org.learnova.lms.repository.course.CourseRepository;
import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegisterIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RoleRepository roleRepository;


    private Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("TEACHER"));
        roleRepository.save(new Role("STUDENT"));
    }

    @AfterEach
    void afterEach() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void shouldRegisterStudent_whenValidInput_thenReturn201() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(faker.internet().emailAddress(), "1234567899");

        mockMvc.perform(
                post("/auth/register/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO))

        ).andExpect(status().isCreated());

    }


    @Test
    void shouldRegisterTeacher_whenValidInput_thenReturn201()  throws Exception {

        RegisterDTO registerDTO = new RegisterDTO(faker.internet().emailAddress(), "1234567899");

        mockMvc.perform(
                post("/auth/register/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO))
        ).andExpect(status().isCreated());
    }


    @Test
    void shouldReturn409_whenRegisteringWithDuplicateEmail() throws Exception {
        RegisterDTO registerDTOFirst = new RegisterDTO(faker.internet().emailAddress(), "1234567899");
        RegisterDTO registerDTOSecond = new RegisterDTO(registerDTOFirst.email(), "1234567899");

        mockMvc.perform(
                post("/auth/register/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTOFirst))
        ).andExpect(status().isCreated());

        mockMvc.perform(
                post("/auth/register/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTOSecond))
        ).andExpect(status().isConflict());
    }

    @Test
    void  shouldReturn400_whenInvalidInputProvided() throws Exception {
        List<RegisterDTO> invalidDtos = List.of(
                new RegisterDTO("not-an-email", "123"),
                new RegisterDTO("", "password123"),
                new RegisterDTO("valid@email.com", "")
        );

        for (RegisterDTO dto : invalidDtos) {
            mockMvc.perform(post("/auth/register/teachers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }
    }



}