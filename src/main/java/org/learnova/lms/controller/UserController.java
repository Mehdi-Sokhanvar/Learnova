package org.learnova.lms.controller;

import org.learnova.lms.dto.request.UserRequestDTO;
import org.learnova.lms.dto.response.SuccessResponse;
import org.learnova.lms.dto.response.UserResponseDTO;
import org.learnova.lms.service.user.UserService;
import org.learnova.lms.util.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> allUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/pending")
    public ResponseEntity<List<UserResponseDTO>> userPending() {
        return new ResponseEntity<>(userService.getPendingUsers(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/{id}/edit")
    public ResponseEntity<?> editUser(@PathVariable Long id,@RequestBody UserRequestDTO userRequest) {
        userService.updateUser(id, userRequest);
        return new ResponseEntity<>(String.format(Messages.USER_UPDATE_SUCCESS, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponse> approvedUser(@PathVariable Long id) {
        userService.approvedUser(id);
        return new ResponseEntity<>(new SuccessResponse(
                String.format("SUCCESFULLY APPROVED USER BY ID %s ",id)), HttpStatus.OK);
    }

}
