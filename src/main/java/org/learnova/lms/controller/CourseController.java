package org.learnova.lms.controller;

import jakarta.validation.Valid;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.dto.ApiResponse;
import org.learnova.lms.dto.response.CourseCreatedResponseDTO;
import org.learnova.lms.dto.response.SuccessResponse;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.dto.request.CourseRequestDTO;

import org.learnova.lms.dto.response.StudentResponse;
import org.learnova.lms.dto.request.EnrollmentRoleForUser;
import org.learnova.lms.service.course.CourseService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    private final MessageSource messageSource;

    public CourseController(CourseService courseService, MessageSource messageSource) {
        this.courseService = courseService;
        this.messageSource = messageSource;
    }


    @PostMapping
    public ResponseEntity<CourseCreatedResponseDTO>addCourse(@Valid @RequestBody CourseRequestDTO course,
                                                             Locale locale) {
        Optional<Course> createdCourse = courseService.addCourse(course);
        String msg = messageSource.getMessage(
                "create.course.success",
                null,
                locale
        );
        CourseCreatedResponseDTO courseResponseDTO = new CourseCreatedResponseDTO(createdCourse.get().getTitle(),
                msg,
                createdCourse.get().getStartDate(),
                createdCourse.get().getEndDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseResponseDTO);
    }


    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse> assignRole(@Valid @RequestBody EnrollmentRoleForUser user, Locale locale) {
        courseService.assignUserRoleInCourses(user);
        String message = messageSource.getMessage(
                "course.user.role.assigned",
                new Object[]{user.userId(),user.courseId(), null},
                locale
        );

        return ResponseEntity.ok(new ApiResponse(true,message));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> editCourse(@PathVariable Long id, @Valid @RequestBody CourseRequestDTO course,Locale locale) {
        courseService.updateCourse(id, course);

        String message = messageSource.getMessage(
                "course.edit.success",
                null,
                locale
        );
        return ResponseEntity.ok(new ApiResponse(true,message));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long id,Locale locale) {
        courseService.deleteCourse(id);
        String message = messageSource.getMessage(
                "course.delete.success",
                null,
                locale
        );
        return ResponseEntity.ok(new ApiResponse(true,message));
    }


    @DeleteMapping("/{userId}/course/{courseId}")
    public ResponseEntity<SuccessResponse> deleteUserFromCourse(@PathVariable Long userId,
                                                                @PathVariable Long courseId,
                                                                Locale locale) {
        courseService.deleteUserFromCourse(userId, courseId);
        String message = messageSource.getMessage(
                "course.student.delete.success",
                new Object[]{userId, courseId},
                locale
        );
        return ResponseEntity.ok(new SuccessResponse(message));
    }

    @GetMapping("/students/{course_id}")
    public ResponseEntity<List<StudentResponse>> listStudentFromCourse(@PathVariable Long course_id) {

        return new ResponseEntity<>(courseService.listStudentsForCourse(course_id), HttpStatus.OK);
    }




}


