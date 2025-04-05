package org.learnova.lms.service.student;

import org.learnova.lms.domain.user.Role;
import org.learnova.lms.domain.user.Student;
import org.learnova.lms.dto.request.RegisterDTO;
import org.learnova.lms.exception.RoleNotFoundException;
import org.learnova.lms.exception.UserExistInDataBase;

import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.StudentRepository;

import org.learnova.lms.util.Messages;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class StudentServiceImpl implements StudentService {


    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void register(RegisterDTO user) {
        Role roleStudent = roleRepository.findByName("ROLE_STUDENT").orElseThrow(() ->
                new RoleNotFoundException(Messages.ROLE_NOT_FOUND));

        studentRepository.findStudentByEmail(user.email()).ifPresentOrElse(
                student -> {
                    throw new UserExistInDataBase(Messages.EMAIL_EXIST);
                },
                () -> {
                    studentRepository.save(new Student(
                            user.email(),
                            passwordEncoder.encode(user.password()),
                            user.email(),
                            roleStudent
                    ));

                }
        );
    }
}
