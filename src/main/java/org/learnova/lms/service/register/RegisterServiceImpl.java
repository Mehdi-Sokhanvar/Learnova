package org.learnova.lms.service.register;

import org.learnova.lms.domain.user.AppUser;

import org.learnova.lms.domain.user.Role;
import org.learnova.lms.domain.user.Student;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.request.RegisterDTO;
import org.learnova.lms.exception.RoleNotFoundException;
import org.learnova.lms.exception.UserExistInDataBase;
import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.UserRepository;
import org.learnova.lms.util.Messages;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterServiceImpl implements RegisterService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void registerStudent(RegisterDTO user) {
        registerUser(user, "ROLE_STUDENT", Student::new);
    }

    @Override
    public void registerTeacher(RegisterDTO user) {
        registerUser(user, "ROLE_TEACHER", Teacher::new);
    }


    private <T extends AppUser> void registerUser(RegisterDTO user, String role, UserFactory<T> userFactory) {
        isEmailExist(user.email());
        Role roleExist = isRoleExist(role);
        String encodedPassword=passwordEncoder.encode(user.password());
        T newUser = userFactory.create(user.email(), encodedPassword, user.email(), roleExist);
        userRepository.save(newUser);
    }

    //todo: is it true line 47

    private void isEmailExist(String email) {
       if (userRepository.existsByEmail(email)) {
           throw new UserExistInDataBase(Messages.EMAIL_EXIST);
       }
    }


    private Role isRoleExist(String role) {
        return roleRepository.findByName(role).orElseThrow(()->new RoleNotFoundException(Messages.ROLE_NOT_FOUND));
    }
}
