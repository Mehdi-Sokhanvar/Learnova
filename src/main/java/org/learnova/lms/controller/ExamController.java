package org.learnova.lms.controller;

import jakarta.validation.Valid;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.*;
import org.learnova.lms.dto.request.ExamRequestDTO;
import org.learnova.lms.dto.response.ExamResponseDTO;
import org.learnova.lms.service.exam.ExamService;
import org.learnova.lms.service.login.CustomUserDetails;
import org.learnova.lms.util.Messages;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {

    private final ExamService examService;
    private final MessageSource messageSource;

    public ExamController(ExamService examService, MessageSource messageSource) {
        this.examService = examService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<ApiResponse> addExam(@RequestBody @Valid ExamRequestDTO exam
            , Authentication authentication, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        examService.addExam(teacher, exam);
        String message = messageSource.getMessage("exam.add.success", null, locale);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, message));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> editExam(@PathVariable Long id, @RequestBody ExamRequestDTO exam,
                                                Authentication authentication, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser teacher = userDetails.getUser();
        examService.updateExam(teacher, id, exam);

        String message = messageSource.getMessage("exam.update.success", new Object[]{id}, locale);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteExam(@PathVariable Long id, Authentication authentication, Locale locale) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser teacher = userDetails.getUser();
        examService.deleteExam(id, teacher.getId());
        String message = messageSource.getMessage("exam.delete.success", null, locale);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }


    @PreAuthorize("hasAnyRole('TEACHER','STUDENT')")
    @GetMapping("/{id}/all")
    public ResponseEntity<List<ExamResponseDTO>> getAllExams(@PathVariable Long id, Principal principal) {
        AppUser user = getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.examListInCourse(id, user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{examId}/start")
    public ResponseEntity<ExamSessionResponseDTO> startExam(@PathVariable("examId") Long examId, Principal principal, Locale locale) {
        AppUser user = getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.startExam(examId, user.getId(), locale), HttpStatus.CREATED);
    }


    @GetMapping("/exam-sessions/{sessionId}/current")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QuestionResponseDTO> getCurrentQuestion(@PathVariable Long sessionId, Principal principal, Locale locale) {
        AppUser studentInExam = getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.getCurrentQuestion(sessionId, studentInExam, locale), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/exam-sessions/{sessionId}/navigate")
    public void navigateQuestion(@PathVariable Long sessionId, @RequestParam String direction, Principal principal, Locale locale) {
        AppUser studentInExam = getUser((UsernamePasswordAuthenticationToken) principal);
        examService.changeCurrentQuestion(sessionId, direction, studentInExam, locale);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/exam-sessions/{sessionId}/answers")
    public void saveAnswer(@PathVariable Long sessionId, @RequestBody AnswerDTO answer, Principal principal, Locale locale) {
        AppUser studentInExam = getUser((UsernamePasswordAuthenticationToken) principal);
        examService.saveAnswerStudent(sessionId, answer, studentInExam, locale);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/exam-session/{examId}/all/answers")
    public ResponseEntity<List<AllAnswersQuestionDTO>> AllQuestionAnswerStudent(@PathVariable Long examId, Principal principal, Locale locale) {
        AppUser studentInExam = getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.getAllAnswerQuestionStudent(examId, studentInExam, locale), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/exam-sessions/{sessionId}/submit")
    public void submitExam(@PathVariable Long sessionId, Principal principal, Locale locale) {
        AppUser studentInExam = getUser((UsernamePasswordAuthenticationToken) principal);
        examService.submitSessionExam(sessionId, studentInExam, locale);
    }

    @PreAuthorize("hasRole('{TEACHER,ADMIN}')")
    @PostMapping("/detail/{examId}/report")
    public ResponseEntity<TeacherReportAsStudent> detailStudentExamInfo(@PathVariable Long examId, @RequestParam("studentId") Long studentId, Principal principal, Locale locale) {
        AppUser teacher = getUser((UsernamePasswordAuthenticationToken) principal);

        return new ResponseEntity<>(examService.getAllDetailReportAsStudent(examId, studentId, teacher, locale), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('{TEACHER,ADMIN}')")
    @GetMapping("/continue/{examId}")
    public ResponseEntity<ExamSessionResponseDTO> detailSessionStudent(@PathVariable Long examId, Principal principal, Locale locale) {
        AppUser student = getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.continueExam(examId, student, locale), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('{TEACHER,ADMIN}')")
    @PostMapping("/essayQuestion/{examId}")
    public void assignScoreToEssayQuestion(@PathVariable Long examId, @RequestBody assignScoreDTO requestDTO
            , Principal principal, Locale locale) {
        AppUser student = getUser((UsernamePasswordAuthenticationToken) principal);
        examService.assignScoreToEssayQuestion(examId, requestDTO, locale);
    }

    @PreAuthorize("hasRole('{TEACHER,ADMIN}')")
    @GetMapping("{examId}/report")
    public ResponseEntity<List<ReportDTO>> reportExam(@PathVariable Long examId
            , Principal principal, Locale locale) {
        AppUser teacher = getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.getReport(examId, teacher), HttpStatus.OK);
    }

    private static AppUser getUser(UsernamePasswordAuthenticationToken principal) {
        CustomUserDetails userDetails = (CustomUserDetails) principal.getPrincipal();
        AppUser user = userDetails.getUser();
        return user;
    }
}

