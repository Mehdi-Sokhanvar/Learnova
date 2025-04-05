package org.learnova.lms.service.teacher;

import org.learnova.lms.domain.user.Role;
import org.learnova.lms.domain.user.Teacher;
import org.learnova.lms.dto.request.RegisterDTO;
import org.learnova.lms.exception.RoleNotFoundException;
import org.learnova.lms.exception.UserExistInDataBase;

import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.TeacherRepository;

import org.learnova.lms.util.Messages;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public TeacherServiceImpl(TeacherRepository teacherRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterDTO user) {
        Role roleTeacher = roleRepository.findByName("ROLE_TEACHER").orElseThrow(() ->
                new RoleNotFoundException(Messages.ROLE_NOT_FOUND));

        teacherRepository.findTeacherByEmail(user.email()).ifPresentOrElse(
                teacher ->
                {
                    throw new UserExistInDataBase(Messages.EMAIL_EXIST);
                },
                () ->
                {
                    teacherRepository.save(new Teacher(
                            user.email(),
                            passwordEncoder.encode(user.password()),
                            user.email(),
                            roleTeacher
                    ));

                }
        );

    }
}
