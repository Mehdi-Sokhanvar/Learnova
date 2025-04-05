package org.learnova.lms.controller;

import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.*;
import org.learnova.lms.dto.request.ExamRequestDTO;
import org.learnova.lms.dto.response.ExamResponseDTO;
import org.learnova.lms.service.exam.ExamService;
import org.learnova.lms.service.login.CustomUserDetails;
import org.learnova.lms.util.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/add")
    public ResponseEntity<?> addExam(@RequestBody ExamRequestDTO exam, Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Teacher teacher = (Teacher) userDetails.getUser();
        examService.addExam(teacher, exam);
        return new ResponseEntity<>(Messages.ADD_EXAM_SUCCESSFULLY, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PutMapping("/{id}/edit")
    public ResponseEntity<?> editExam(@PathVariable Long id, @RequestBody ExamRequestDTO exam, Principal principal) {
        AppUser teacher = getUser((UsernamePasswordAuthenticationToken) principal);
        examService.updateExam(teacher, id, exam);
        return new ResponseEntity<>(String.format(Messages.EXAM_UPDATE_SUCCESFULLY, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExam(@PathVariable Long id, Principal principal) {
        AppUser teacher = getUser((UsernamePasswordAuthenticationToken) principal);
        examService.deleteExam(id, teacher.getId());
        return new ResponseEntity<>(String.format(Messages.DELETE_EXAM_SUCCESFULLY, id), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT')")
    @GetMapping("/{id}/all")
    public ResponseEntity<List<ExamResponseDTO>> getAllExams(@PathVariable Long id, Principal principal) {
        AppUser user = getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.examListInCourse(id, user), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/{examId}/start")
    public ResponseEntity<ExamSessionResponseDTO> startExam(@PathVariable("examId") Long examId, Principal principal, Locale locale) {
        AppUser user = getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.startExam(examId, user.getId(), locale), HttpStatus.CREATED);
    }


    @GetMapping("/exam-sessions/{sessionId}/current")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<QuestionResponseDTO> getCurrentQuestion(@PathVariable Long sessionId,Principal principal,Locale locale){
        AppUser studentInExam=getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.getCurrentQuestion(sessionId,studentInExam,locale),HttpStatus.OK);
    }

    @PostMapping("/exam-sessions/{sessionId}/navigate")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void navigateQuestion(@PathVariable Long sessionId,@RequestParam String direction, Principal principal,Locale locale){
        AppUser studentInExam=getUser((UsernamePasswordAuthenticationToken) principal);
        examService.changeCurrentQuestion(sessionId,direction,studentInExam,locale);
    }

    @PostMapping("/exam-sessions/{sessionId}/answers")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void saveAnswer(@PathVariable Long sessionId, @RequestBody AnswerDTO answer, Principal principal, Locale locale){
        AppUser studentInExam=getUser((UsernamePasswordAuthenticationToken) principal);
        examService.saveAnswerStudent(sessionId,answer,studentInExam,locale);
    }

    @GetMapping("/exam-session/{examId}/all/answers")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<List<AllAnswersQuestionDTO>> AllQuestionAnswerStudent(@PathVariable Long examId, Principal principal,Locale locale){
        AppUser studentInExam=getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.getAllAnswerQuestionStudent(examId,studentInExam,locale),HttpStatus.OK);
    }//todo : writie better this code


    @PostMapping("/exam-sessions/{sessionId}/submit")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void submitExam(@PathVariable Long sessionId, Principal principal,Locale locale){
        AppUser studentInExam=getUser((UsernamePasswordAuthenticationToken) principal);
        examService.submitSessionExam(sessionId,studentInExam,locale);
    }


    @PostMapping("/detail/{examId}/report")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<TeacherReportAsStudent> detailStudentExamInfo(@PathVariable Long examId,@RequestParam("studentId") Long studentId, Principal principal,Locale locale){
        AppUser teacher=getUser((UsernamePasswordAuthenticationToken) principal);

        return new ResponseEntity<>(  examService.getAllDetailReportAsStudent(examId,studentId,teacher,locale),HttpStatus.OK);
    }

    @GetMapping("/continue/{examId}")
    public ResponseEntity<ExamSessionResponseDTO> detailSessionStudent(@PathVariable Long examId, Principal principal,Locale locale){
        AppUser student=getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.continueExam(examId,student,locale),HttpStatus.OK);
    }


    @PostMapping("/essayQuestion/{examId}")
    public void assignScoreToEssayQuestion(@PathVariable Long examId,@RequestBody assignScoreDTO requestDTO
            ,Principal principal,Locale locale){
        AppUser student=getUser((UsernamePasswordAuthenticationToken) principal);
        examService.assignScoreToEssayQuestion(examId,requestDTO,locale);
    }

    @GetMapping("{examId}/report")
    public ResponseEntity<List<ReportDTO>> reportExam(@PathVariable Long examId
            ,Principal principal,Locale locale){
        AppUser teacher=getUser((UsernamePasswordAuthenticationToken) principal);
        return new ResponseEntity<>(examService.getReport(examId,teacher),HttpStatus.OK);
    }

    private static AppUser getUser(UsernamePasswordAuthenticationToken principal) {
        CustomUserDetails userDetails = (CustomUserDetails) principal.getPrincipal();
        AppUser user = userDetails.getUser();
        return user;
    }
}

