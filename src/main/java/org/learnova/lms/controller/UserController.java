package org.learnova.lms.controller;

import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.dto.request.UserRequestDTO;
import org.learnova.lms.dto.response.CourseResponseDTO;
import org.learnova.lms.dto.response.SuccessResponse;
import org.learnova.lms.dto.response.UserResponseDTO;
import org.learnova.lms.service.course.CourseService;
import org.learnova.lms.service.login.CustomUserDetails;
import org.learnova.lms.service.user.UserService;
import org.learnova.lms.util.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


/**
 * REST controller for managing users and their related operations.
 * Provides endpoints to list all users, pending users, approve users,
 * edit user details, and list courses for the authenticated teacher.
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final CourseService courseService;

    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }


    /**
     * Retrieve all users in the system.
     *
     * @return a list of all users wrapped in ResponseEntity with HTTP status 200.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> allUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    /**
     * Retrieve all users whose accounts are pending approval.
     *
     * @return a list of pending users with HTTP status 200.
     */
    @GetMapping("/pending")
    public ResponseEntity<List<UserResponseDTO>> userPending() {
        return new ResponseEntity<>(userService.getPendingUsers(), HttpStatus.OK);
    }

    /**
     * Edit a user's information by ID.
     *
     * @param id the ID of the user to update
     * @param userRequest the new user data
     * @return a success message with HTTP status 200.
     */
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> editUser(@PathVariable Long id,@RequestBody UserRequestDTO userRequest) {
        userService.updateUser(id, userRequest);
        return new ResponseEntity<>(String.format(Messages.USER_UPDATE_SUCCESS, id), HttpStatus.OK);
    }

    /**
     * Approve a user by ID.
     *
     * @param id the ID of the user to approve
     * @return a success message wrapped in SuccessResponse with HTTP status 200.
     */
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponse> approvedUser(@PathVariable Long id) {
        userService.approvedUser(id);
        return new ResponseEntity<>(new SuccessResponse(
                String.format("SUCCESFULLY APPROVED USER BY ID %s ",id)), HttpStatus.OK);
    }

    /**
     * List all courses assigned to the currently authenticated teacher.
     *
     * @param principal the security principal representing the logged-in user
     * @return a list of courses for the authenticated user with HTTP status 200.
     */

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponseDTO>> listTeacherCourses(Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails)
                ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        AppUser user = userDetails.getUser();
        return new ResponseEntity<>(courseService.listOfUserCourses(user), HttpStatus.OK);
    }


}
