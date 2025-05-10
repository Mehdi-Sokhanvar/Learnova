package org.learnova.lms.controller;

import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.ApiResponse;
import org.learnova.lms.dto.QuestionResponseDTO;
import org.learnova.lms.dto.request.QuestionDTO;
import org.learnova.lms.dto.request.QuestionRequestDto;
import org.learnova.lms.service.login.CustomUserDetails;
import org.learnova.lms.service.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    @Autowired
    private MessageSource messageSource;

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse> createQuestion(@RequestBody QuestionDTO dto, Principal principal, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        AppUser teacher = userDetails.getUser();
        questionService.addQuestion(teacher, dto);
        String message = messageSource.getMessage("question.add.success", null, locale);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/add/exam")
    public ResponseEntity<ApiResponse> addQuestionTOCourse(@RequestBody QuestionRequestDto dto, Principal principal, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        questionService.addQuestionToExam(teacher, dto);
        String message = messageSource.getMessage("question.exam.add.success", null, locale);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }


    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{questionId}")
    public ResponseEntity<ApiResponse> deleteQuestion(@PathVariable Long questionId, Principal principal, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        questionService.deleteQuestion(teacher, questionId);
        String message = messageSource.getMessage("question.delete.success", null, locale);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{questionId}/exam/{examId}")
    public ResponseEntity<ApiResponse> deleteQuestionFromExam(@PathVariable Long questionId, @PathVariable Long examId, Principal principal, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        questionService.deleteQuestionFromExam(teacher, questionId, examId);
        String message = messageSource.getMessage("question.exam.delete.success", null, locale);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{examId}/exam")
    public ResponseEntity<List<QuestionResponseDTO>> getExamQuestions(@PathVariable Long examId, Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        return new ResponseEntity<>(questionService.findAllExamQuestions(teacher, examId), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher")
    public ResponseEntity<List<QuestionResponseDTO>> getTeacherQuestion(Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        return new ResponseEntity<>(questionService.findTeacherQuestion(teacher), HttpStatus.OK);
    }


}


