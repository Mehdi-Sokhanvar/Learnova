package org.learnova.lms.controller;

import jakarta.validation.Valid;
import org.learnova.lms.config.jwt.JwtTokenProvider;
import org.learnova.lms.dto.response.LoginResponse;
import org.learnova.lms.dto.request.LoginDTO;

import org.learnova.lms.service.login.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserDetailsService loginService;
    private final AuthenticationManager authenticationManager;
    private final  JwtTokenProvider jwtTokenProvider;

    public LoginController(UserDetailsService loginService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.loginService = loginService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginDTO loginDTO) {
        var auth = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());
        Authentication authenticate = authenticationManager.authenticate(auth);
        loginService.loadUserByUsername(loginDTO.username());
        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
