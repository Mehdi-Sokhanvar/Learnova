//package org.learnova.lms.service.impl;
//
//import org.learnova.lms.domain.user.AppUser;
//import org.learnova.lms.domain.user.Role;
//import org.learnova.lms.domain.user.Student;
//import org.learnova.lms.domain.user.Teacher;
//import org.learnova.lms.controller.request.dto.RegisterDTO;
//import org.learnova.lms.exception.RoleNotFoundException;
//import org.learnova.lms.exception.UserExistInDataBase;

//import org.learnova.lms.repository.user.StudentRepository;
//import org.learnova.lms.repository.user.TeacherRepository;
//import org.learnova.lms.repository.user.UserRepository;
//import org.learnova.lms.service.RegisterService;
//
////import org.springframework.security.crypto.password.PasswordEncoder;
//import org.learnova.lms.util.Messages;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class RegisterServiceImpl implements RegisterService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final TeacherRepository teacherRepository;
//    private final StudentRepository studentRepository;
//    private final RoleRepository roleRepository;
//
//    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
//                               TeacherRepository teacherRepository, StudentRepository studentRepository
//            , RoleRepository roleRepository) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.teacherRepository = teacherRepository;
//        this.studentRepository = studentRepository;
//        this.roleRepository = roleRepository;
//    }
//
//
//    @Override
//    public void register(RegisterDTO registerDTO) {
////        //todo : if user foudn send exception
//        Optional<AppUser> userFounded = userRepository.findUserByEmail(registerDTO.email());
//        if (userFounded.isPresent()) {
//            throw new UserExistInDataBase(Messages.EMAIL_EXIST);
//        }
//
//        //todo : if request has maneger role throw exceptions
//
//        Role roleFound = roleRepository.findByName(registerDTO.role()).orElseThrow(() ->
//                new RoleNotFoundException(Messages.ROLE_NOT_FOUND));
//        if (registerDTO.role().equals("TEACHER")) {
//            teacherRepository.save(
//                    new Teacher(
//                            registerDTO.email(),
//                            passwordEncoder.encode(registerDTO.password()),
//                            registerDTO.email(),
//                            roleFound
//                    )
//            );
//        }
//        if (registerDTO.role().equals("STUDENT")) {
//            studentRepository.save(
//                    new Student(
//                            registerDTO.email(),
//                            passwordEncoder.encode(registerDTO.password()),
//                            registerDTO.email(),
//                            roleFound
//                    )
//            );
//        }
//    }
//
//
//}
