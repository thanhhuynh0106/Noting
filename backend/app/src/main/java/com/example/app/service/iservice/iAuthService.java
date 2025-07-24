package com.example.app.service.iservice;

import com.example.app.dto.LoginDTO;
import com.example.app.dto.RegisterDTO;
import com.example.app.dto.ResponseDTO;
import com.example.app.entity.User;
import jakarta.servlet.http.HttpSession;

public interface iAuthService {
    ResponseDTO login(LoginDTO loginDTO, HttpSession session);
    ResponseDTO register(User user);
    ResponseDTO logout(HttpSession session);
}
