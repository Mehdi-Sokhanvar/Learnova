package org.learnova.lms.controller;

import jakarta.validation.Valid;
import org.learnova.lms.domain.course.Course;
import org.learnova.lms.dto.response.ApiResponse;
import org.learnova.lms.dto.response.CourseCreatedResponseDTO;
import org.learnova.lms.dto.response.SuccessResponse;
import org.learnova.lms.dto.request.CourseRequestDTO;
import org.learnova.lms.dto.response.StudentResponse;
import org.learnova.lms.dto.request.EnrollmentRoleForUser;
import org.learnova.lms.service.course.CourseService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * REST controller for managing courses.
 * Provides endpoints for creating, editing, deleting courses,
 * assigning roles to users in courses, and listing students.
 */
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    private final MessageSource messageSource;

    public CourseController(CourseService courseService, MessageSource messageSource) {
        this.courseService = courseService;
        this.messageSource = messageSource;
    }


    // ... constructor injection ...

    /**
     * Adds a new course. Only accessible by admins.
     *
     * @param course the course data to create
     * @param locale the locale to fetch localized messages
     * @return ResponseEntity containing the created course details
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Assigns a role to a user in a course. Only accessible by admins.
     *
     * @param user the user and course information with the role
     * @param locale the locale to fetch localized messages
     * @return ResponseEntity with a success message
     */
    @PreAuthorize("hasRole('ADMIN')")
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


    /**
     * Updates an existing course by ID. Only accessible by admins.
     *
     * @param id the ID of the course to update
     * @param course the updated course data
     * @param locale the locale to fetch localized messages
     * @return ResponseEntity with a success message
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> editCourse(@PathVariable("id") Long id, @Valid @RequestBody CourseRequestDTO course,Locale locale) {
        courseService.updateCourse(id, course);

        String message = messageSource.getMessage(
                "course.edit.success",
                null,
                locale
        );
        return ResponseEntity.ok(new ApiResponse(true,message));
    }


    /**
     * Deletes a course by ID. Only accessible by admins.
     *
     * @param id the ID of the course to delete
     * @param locale the locale to fetch localized messages
     * @return ResponseEntity with a success message
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable("id") Long id,Locale locale) {
        courseService.deleteCourse(id);
        String message = messageSource.getMessage(
                "course.delete.success",
                null,
                locale
        );
        return ResponseEntity.ok(new ApiResponse(true,message));
    }

    /**
     * Deletes a user from a specific course. Only accessible by admins.
     *
     * @param userId the ID of the user to remove
     * @param courseId the ID of the course
     * @param locale the locale to fetch localized messages
     * @return ResponseEntity with a success message
     */
    @PreAuthorize("hasRole('ADMIN')")
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


    /**
     * Lists all students enrolled in a course. Accessible by teachers and admins.
     *
     * @param course_id the ID of the course
     * @return ResponseEntity containing the list of students
     */
    @PreAuthorize("hasRole('{TEACHER,ADMIN}')")
    @GetMapping("/students/{course_id}")
    public ResponseEntity<List<StudentResponse>> listStudentFromCourse(@PathVariable Long course_id) {

        return new ResponseEntity<>(courseService.listStudentsForCourse(course_id), HttpStatus.OK);
    }
}


