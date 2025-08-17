package org.learnova.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Role;
import org.learnova.lms.dto.request.LoginDTO;
import org.learnova.lms.exception.RoleNotFoundException;
import org.learnova.lms.repository.course.CourseRepository;
import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.UserRepository;
import org.learnova.lms.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginIntegrationTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CourseRepository courseRepository;

    private Faker faker = new Faker();


    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void givenValidCredentials_whenLogin_thenReturns200Ok() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUserName("email@email");
        appUser.setEmail("email@email");
        appUser.setRole(new Role("TEACHER"));
        appUser.setPassword(passwordEncoder.encode("123456789"));
        AppUser userSaved = userRepository.save(appUser);

        LoginDTO loginDTO =
                new LoginDTO(userSaved.getUserName(), "123456789");

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
        ).andExpect(status().isOk());

    }

    @Test
    void givenNonExistingUser_whenLogin_thenReturns400BadRequest() throws Exception {

        LoginDTO loginDTO = new LoginDTO(faker.internet().emailAddress(), "123456789");

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
        ).andExpect(status().isBadRequest());

    }

    @Test
    void givenWrongPassword_whenLogin_thenReturns400BadRequest() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUserName("email@email");
        appUser.setEmail("email@email");
        appUser.setPassword(passwordEncoder.encode("correctpassword"));
        userRepository.save(appUser);

        LoginDTO loginDTO = new LoginDTO("email@email", "wrongpassword");

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidInput_whenLogin_thenReturns400BadRequest() throws Exception {
        LoginDTO loginDTO = new LoginDTO("notanemail", "");

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
        ).andExpect(status().isBadRequest());
    }
}