package org.learnova.lms.controller;

import jakarta.validation.Valid;
import org.learnova.lms.dto.request.RegisterDTO;
import org.learnova.lms.service.register.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *  @author: this is mehdi
 */
@RestController
@RequestMapping("/api/register")
public class RegisterController {


    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/teacher")
    public ResponseEntity<?> registerStudent(@Valid
                                             @RequestBody RegisterDTO user) {

        registerService.registerStudent(user);
        return new ResponseEntity<>("TEACHER REGISTER SUCCESFULLY", HttpStatus.CREATED);
    }

    @PostMapping("/student")
    public ResponseEntity<?> registerTeacher(@Valid
                                             @RequestBody RegisterDTO user) {
        registerService.registerTeacher(user);
        return new ResponseEntity<>("STUDENT REGISTER SUCCESFULLY", HttpStatus.CREATED);
    }


}
