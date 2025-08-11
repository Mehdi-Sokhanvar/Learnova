package org.learnova.lms.controller;

import jakarta.validation.Valid;

import org.learnova.lms.config.jwt.JwtService;
import org.learnova.lms.domain.user.Role;
import org.learnova.lms.dto.response.AuthenticationResponse;
import org.learnova.lms.dto.response.LoginResponse;
import org.learnova.lms.dto.request.LoginDTO;

import org.learnova.lms.service.login.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDTO loginDTO) {
        Authentication authenticate = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.username(),
                        loginDTO.password()
                )
        );
        String role = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");


        final String accessToken = jwtService.generateAccessToken(loginDTO.username(), role);
        final String refreshToken = jwtService.generateRefreshToken(loginDTO.username(), role);
        final String tokenType = "Bearer";
        return ResponseEntity.ok(new AuthenticationResponse.Builder(accessToken)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                    .build());
    }
}
