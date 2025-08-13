package org.learnova.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.dto.request.RegisterDTO;
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
class RegisterControllerTest {

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
    void registerStudent() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(faker.internet().emailAddress(), "1234567899");

        mockMvc.perform(
                post("/api/register/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO))

        ).andExpect(status().isCreated());

    }


    @Test
    void registerTeacher() throws Exception {

        RegisterDTO registerDTO = new RegisterDTO(faker.internet().emailAddress(), "1234567899");

        mockMvc.perform(
                post("/api/register/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO))
        ).andExpect(status().isCreated());
    }


    @Test
    void conflict_User_Register() throws Exception {
        RegisterDTO registerDTOFirst = new RegisterDTO(faker.internet().emailAddress(), "1234567899");
        RegisterDTO registerDTOSecond = new RegisterDTO(registerDTOFirst.email(), "1234567899");

        mockMvc.perform(
                post("/api/register/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTOFirst))
        );
        mockMvc.perform(
                post("/api/register/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTOSecond))
        ).andExpect(status().isConflict());


    }


    @Test
    void registerUser_invalidInput() throws Exception {
        List<RegisterDTO> invalidDtos = List.of(
                new RegisterDTO("not-an-email", "123"),
                new RegisterDTO("", "password123"),
                new RegisterDTO("valid@email.com", "")
        );

        for (RegisterDTO dto : invalidDtos) {
            mockMvc.perform(post("/api/register/teachers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }
    }



}