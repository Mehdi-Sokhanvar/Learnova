package org.learnova.lms;


import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Role;

import org.learnova.lms.repository.role.RoleRepository;
import org.learnova.lms.repository.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    public DataLoader(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("STUDENT"));
            roleRepository.save(new Role("TEACHER"));
            roleRepository.save(new Role("ADMIN"));
        }


        AppUser use = new AppUser("admin@gmail.com", passwordEncoder.encode("12345678"), "admin@gmail.com", new Role("ADMIN"));
        userRepository.save(use);
    }


}
