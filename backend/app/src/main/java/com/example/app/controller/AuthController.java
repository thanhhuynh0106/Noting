package com.example.app.controller;

import com.example.app.dto.LoginDTO;
import com.example.app.dto.ResponseDTO;
import com.example.app.entity.User;
import com.example.app.service.impl.AuthService;
import com.example.app.service.iservice.iAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private iAuthService authService;

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        String expectedCaptcha = (String) session.getAttribute("captcha");
        if (expectedCaptcha == null || !expectedCaptcha.equalsIgnoreCase(loginDTO.getCaptcha())) {
            ResponseDTO response = new ResponseDTO();
            response.setMessage("Invalid captcha");
            response.setStatus("400");
            return response;
        }

        return authService.login(loginDTO, session);
    }

    @PostMapping("/register")
    public ResponseDTO register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/logout")
    public ResponseDTO logout(HttpSession session) {
        return authService.logout(session);
    }
}

