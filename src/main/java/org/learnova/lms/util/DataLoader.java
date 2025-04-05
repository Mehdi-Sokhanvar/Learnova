package org.learnova.lms;

import org.learnova.lms.domain.user.Role;

import org.learnova.lms.repository.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_STUDENT"));
            roleRepository.save(new Role("ROLE_TEACHER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
    }


}
