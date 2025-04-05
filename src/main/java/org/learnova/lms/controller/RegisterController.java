package org.learnova.lms.controller;

import jakarta.validation.Valid;
import org.learnova.lms.dto.request.RegisterDTO;
import org.learnova.lms.service.student.StudentService;
import org.learnova.lms.service.teacher.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

/**
 *  @author: this is mehdi
 */
@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {

    private final TeacherService teacherService;
    private final StudentService studentService;

    public RegisterController(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @PostMapping("/teacher")
    public ResponseEntity<?> registerStudent(@Valid
                                             @RequestBody RegisterDTO user) {

        teacherService.register(user);
        return new ResponseEntity<>("TEACHER REGISTER SUCCESFULLY", HttpStatus.CREATED);
    }

    @PostMapping("/student")
    public ResponseEntity<?> registerTeacher(@Valid
                                             @RequestBody RegisterDTO user) {
        studentService.register(user);
        return new ResponseEntity<>("STUDENT REGISTER SUCCESFULLY", HttpStatus.CREATED);
    }


}
