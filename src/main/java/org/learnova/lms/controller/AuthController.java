package org.learnova.lms.controller;

import jakarta.validation.Valid;

import org.learnova.lms.config.jwt.JwtService;
import org.learnova.lms.domain.user.Role;
import org.learnova.lms.dto.request.RegisterDTO;
import org.learnova.lms.dto.response.ApiResponse;
import org.learnova.lms.dto.response.AuthenticationResponse;
import org.learnova.lms.dto.response.LoginResponse;
import org.learnova.lms.dto.request.LoginDTO;

import org.learnova.lms.service.login.CustomUserDetails;
import org.learnova.lms.service.register.RegisterService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * REST controller for authentication and registration endpoints.
 * Handles login and registration for students and teachers.
 */
@RestController
@RequestMapping("/auth/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RegisterService registerService;
    private final MessageSource messageSource;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, RegisterService registerService, MessageSource messageSource) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.registerService = registerService;
        this.messageSource = messageSource;
    }


    // ... constructor injection ...

    /**
     * Authenticates a user and returns an access and refresh JWT token.
     *
     * @param loginDTO the login credentials, must not be null
     * @return a ResponseEntity containing the access and refresh tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDTO loginDTO) {
        Authentication authenticate = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.username(),
                        loginDTO.password()
                )
        );
        String role = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");


        final String accessToken = jwtService.generateAccessToken(loginDTO.username(), role);
        final String refreshToken = jwtService.generateRefreshToken(loginDTO.username(), role);
        final String tokenType = "Bearer";
        return ResponseEntity.ok(new AuthenticationResponse.Builder(accessToken)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                    .build());
    }


    /**
     * Registers a new student in the system.
     *
     * @param user the registration data for the student
     * @param locale the locale to fetch localized messages
     * @return a ResponseEntity containing a success message
     */
    @PostMapping("/register/students")
    public ResponseEntity<ApiResponse> registerStudent(@Valid
                                                       @RequestBody RegisterDTO user, Locale locale) {
        registerService.registerStudent(user);
        String msg = messageSource.getMessage(
                "register.student.success",
                null,
                locale
        );
        ApiResponse body = new ApiResponse(true, msg);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


    /**
     * Registers a new teacher in the system.
     *
     * @param user the registration data for the teacher
     * @param locale the locale to fetch localized messages
     * @return a ResponseEntity containing a success message
     */

    @PostMapping("/register/teachers")
    public ResponseEntity<ApiResponse> registerTeacher(@Valid
                                                       @RequestBody RegisterDTO user, Locale locale) {
        registerService.registerTeacher(user);
        String msg = messageSource.getMessage(
                "register.teacher.success",
                null,
                locale
        );
        ApiResponse body = new ApiResponse(true, msg);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

}
