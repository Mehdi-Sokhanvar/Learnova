package org.learnova.lms.controller;

import jakarta.validation.Valid;
import org.learnova.lms.dto.response.SuccessResponse;
import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.dto.request.CourseRequestDTO;
import org.learnova.lms.dto.response.CourseResponseDTO;
import org.learnova.lms.dto.response.StudentResponse;
import org.learnova.lms.dto.request.EnrollmentRoleForUser;

import org.learnova.lms.service.course.CourseService;
import org.learnova.lms.service.login.CustomUserDetails;
import org.learnova.lms.util.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }



    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@Valid @RequestBody CourseRequestDTO course) {
        return ResponseEntity.ok(courseService.addCourse(course));
    }


    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRoleForUserInCourses(@Valid @RequestBody EnrollmentRoleForUser user) {
        courseService.assignUserRoleInCourses(user);
        return ResponseEntity.ok(String.format(Messages.USER_ASSIGN_COURSE_SUCCESS, user.userId()));
    }


    @PutMapping("/{id}/edit")
    public ResponseEntity<SuccessResponse> editCourse(@PathVariable Long id, @Valid @RequestBody CourseRequestDTO course) {
        courseService.updateCourse(id, course);
        return new ResponseEntity<>(new SuccessResponse(String.format(Messages.COURSE_UPDATE_SUCCESFULY, id)), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(new SuccessResponse(String.format(Messages.DELETE_COURSE_SUCCESFULLY, id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{userId}/course/{courseId}")
    public ResponseEntity<SuccessResponse> deleteUserFromCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        courseService.deleteUserFromCourse(userId, courseId);
        return new ResponseEntity<>(new SuccessResponse(String.format(Messages.SUCCESFULLY_DELETE_STUDENT_FROM_COURSE, userId, courseId)), HttpStatus.OK);
    }

//    todo:feature Teaher in course can see all student
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/students/{course_id}")
    public ResponseEntity<List<StudentResponse>> listStudentFromCourse(@PathVariable Long course_id) {
        return new ResponseEntity<>(courseService.listStudentsForCourse(course_id), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT')" )
    @GetMapping("/user")
    public ResponseEntity<List<CourseResponseDTO>> listTeacherCourses(Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails)
                ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        AppUser user = userDetails.getUser();
        return new ResponseEntity<>(courseService.listOfUserCourses(user), HttpStatus.OK);
    }
}


