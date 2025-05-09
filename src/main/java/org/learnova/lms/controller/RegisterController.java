package org.learnova.lms.controller;

import jakarta.validation.Valid;
import org.learnova.lms.dto.ApiResponse;
import org.learnova.lms.dto.request.RegisterDTO;
import org.learnova.lms.service.register.RegisterService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;


@RestController
@RequestMapping("/api/register")
public class RegisterController {


    private final RegisterService registerService;
    private final MessageSource messageSource;

    public RegisterController(RegisterService registerService, MessageSource messageSource) {
        this.registerService = registerService;
        this.messageSource = messageSource;
    }

    @PostMapping("/students")
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

    @PostMapping("/teachers")
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
