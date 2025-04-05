package org.learnova.lms.service.user;

import org.learnova.lms.dto.request.UserRequestDTO;
import org.learnova.lms.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUser();

    List<UserResponseDTO> getPendingUsers();

    void updateUser(Long id,UserRequestDTO userRequestDTO);

    void approvedUser(Long id);
}
