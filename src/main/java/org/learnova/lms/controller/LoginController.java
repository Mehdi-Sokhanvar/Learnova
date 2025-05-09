package org.learnova.lms.controller;

import org.learnova.lms.config.JwtTokenProvider;
import org.learnova.lms.dto.LoginResponse;
import org.learnova.lms.dto.request.LoginDTO;

import org.learnova.lms.service.login.LoginServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginServiceImpl loginService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(LoginServiceImpl loginService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.loginService = loginService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        var auth = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());
        authenticationManager.authenticate(auth);
        var user = loginService.loadUserByUsername(loginDTO.username());
        String token = jwtTokenProvider.createToken(loginDTO.username(), loginDTO.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
