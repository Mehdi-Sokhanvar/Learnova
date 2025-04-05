package org.learnova.lms.service.user;

import org.learnova.lms.domain.enums.Status;
import org.learnova.lms.dto.request.UserRequestDTO;
import org.learnova.lms.dto.response.UserResponseDTO;
import org.learnova.lms.exception.UserNotFoundException;
import org.learnova.lms.repository.user.UserRepository;


import org.learnova.lms.util.Messages;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//todo : اگر روی اینترفیس بزاریم خوذش تسهصی میده
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDTO> getAllUser() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getUserName(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getStatus()
                )).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getPendingUsers() {
        return userRepository.findAppUserByStatus(Status.PENDING).stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getUserName(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getStatus()
                )).collect(Collectors.toList());
    }

    @Override
    public void updateUser(Long id, UserRequestDTO user) {
        userRepository.findById(id).ifPresentOrElse(
                existingUser -> {
                    existingUser.setUserName(user.username());
                    existingUser.setFirstName(user.firstName());
                    existingUser.setLastName(user.lastName());
                    existingUser.setEmail(user.email());
//                    existingUser(LocalDateTime.now());
                    existingUser.setStatus(user.status());
                    existingUser.setPhone(user.phone());
                    // Save the updated user
                    userRepository.save(existingUser);
                }, () -> {
                    throw new UserNotFoundException(String.format(Messages.USER_NOT_FOUND_BY_THIS_ID, id));
                });
    }

    @Override
    public void approvedUser(Long id) {

    }


}
