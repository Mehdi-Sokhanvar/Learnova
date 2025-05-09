package org.learnova.lms.service.register;

import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.domain.user.Role;

@FunctionalInterface
public interface UserFactory<T extends AppUser>{
    T create(String userName,String password, String email, Role role);
}
