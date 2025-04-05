package org.learnova.lms.controller;

import org.learnova.lms.dto.request.LoginDTO;
import org.learnova.lms.service.impl.LoginServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginServiceImpl loginService;

    public LoginController(LoginServiceImpl loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequestDTO) {
//        try {
//            loginService.authenticate(loginRequestDTO); // بررسی نام کاربری و رمز عبور
//            // در صورت موفقیت: ایجاد توکن JWT یا پاسخ مناسب
//            return ResponseEntity.ok().body("لاگین موفقیتآمیز بود!");
//        } catch (UsernameNotFoundException | BadCredentialsException e) {
//            return ResponseEntity.status(401).body(e.getMessage());
//        }
        return null;
    }
}
