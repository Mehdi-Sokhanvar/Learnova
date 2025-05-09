package org.learnova.lms.service.login;

import org.learnova.lms.domain.user.AppUser;
import org.learnova.lms.repository.user.UserRepository;
import org.learnova.lms.service.login.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public LoginServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new CustomUserDetails(user);
    }

}