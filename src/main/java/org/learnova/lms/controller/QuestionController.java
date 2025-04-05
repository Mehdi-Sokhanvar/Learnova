package org.learnova.lms.controller;

import org.learnova.lms.domain.question.Question;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.QuestionResponseDTO;
import org.learnova.lms.dto.request.QuestionDTO;
import org.learnova.lms.dto.request.QuestionRequestDto;
import org.learnova.lms.service.login.CustomUserDetails;
import org.learnova.lms.service.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @PostMapping
    public ResponseEntity<String> createQuestion(@RequestBody QuestionDTO dto, Principal principal, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        AppUser teacher = userDetails.getUser();
        questionService.addQuestion(teacher, dto);
        return ResponseEntity.ok(messageSource.getMessage("Question.Add", null, locale));
    }




    @PostMapping("/add/exam")
    public ResponseEntity<String> addQuestionTOCourse(@RequestBody QuestionRequestDto dto, Principal principal, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        questionService.addQuestionToExam(teacher, dto);
        return new ResponseEntity<>(messageSource.getMessage("Add.Question.To.Exam", null, locale), HttpStatus.OK);
    }



    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId,Principal principal, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        questionService.deleteQuestion(teacher,questionId);
        return new ResponseEntity<>(messageSource.getMessage("Delete.Question",null,locale),HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}/exam/{examId}")
    public ResponseEntity<String> deleteQuestionFromExam(@PathVariable Long questionId,@PathVariable Long examId,Principal principal, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        questionService.deleteQuestionFromExam(teacher,questionId,examId);
        return new ResponseEntity<>(messageSource.getMessage("Delete.QuestionFromExam",null,locale),HttpStatus.OK);

    }

    @GetMapping("/{examId}/exam")
    public ResponseEntity<List<QuestionResponseDTO>> getExamQuestions(@PathVariable Long examId, Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        return new ResponseEntity<>(questionService.findAllExamQuestions(teacher,examId),HttpStatus.OK);
    }


    @GetMapping("/teacher")
    public ResponseEntity<List<QuestionResponseDTO>> getTeacherQuestion(Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        return new ResponseEntity<>(questionService.findTeacherQuestion(teacher),HttpStatus.OK);
    }


}


