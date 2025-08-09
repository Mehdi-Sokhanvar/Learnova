package org.learnova.lms.controller;

import jakarta.validation.Valid;

import org.learnova.lms.config.jwt.JwtService;
import org.learnova.lms.dto.response.AuthenticationResponse;
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
    private final JwtService jwtService;

    public LoginController(UserDetailsService loginService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.loginService = loginService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDTO loginDTO) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.username(),
                        loginDTO.password()
                )
        );
        final String accessToken = jwtService.generateAccessToken(loginDTO.username());
        final String refreshToken = jwtService.generateRefreshToken(loginDTO.username());
        final String tokenType = "Bearer";
        return ResponseEntity.ok(new AuthenticationResponse.Builder(accessToken)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .build());
    }
}
