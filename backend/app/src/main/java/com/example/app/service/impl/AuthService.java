package com.example.app.service.impl;

import com.example.app.dto.LoginDTO;
import com.example.app.dto.RegisterDTO;
import com.example.app.dto.ResponseDTO;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.service.iservice.iAuthService;
import com.example.app.utils.JwtUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements iAuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseDTO login(LoginDTO loginDTO, HttpSession session) {
        ResponseDTO response = new ResponseDTO();

        String captchaSession = (String) session.getAttribute("captcha");
        if (captchaSession == null || !captchaSession.equalsIgnoreCase(loginDTO.getCaptcha())) {
            response.setStatus("fail");
            response.setMessage("Invalid captcha");
            return response;
        }

        Optional<User> userOpt = Optional.ofNullable(userRepository.findByUserName(loginDTO.getUserName()));
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginDTO.getPassword(), userOpt.get().getPassword())) {
            response.setStatus("fail");
            response.setMessage("Invalid username or password");
            return response;
        }

        User user = userOpt.get();
        String token = jwtUtils.generateToken(user);
        response.setStatus("success");
        response.setMessage("Login successful");
        response.setToken(token);
        response.setUserName(user.getUsername());
        response.setUserRole(user.getUserRole());

        return response;
    }

    @Override
    public ResponseDTO register(User user) {
        ResponseDTO responseDTO = new ResponseDTO();

        if (user.getUserRole() == null || user.getUserRole().isEmpty()) {
            user.setUserRole("USER");
        }

        if (userRepository.existsByUserName(user.getUsername())) {
            responseDTO.setMessage("Username already exists");
            responseDTO.setStatus("400");
            return responseDTO;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new java.util.Date());

        User savedUser = userRepository.save(user);

        String token = jwtUtils.generateToken(savedUser);

        responseDTO.setMessage("Registration successful");
        responseDTO.setStatus("200");
        responseDTO.setToken(token);
        responseDTO.setUserName(savedUser.getUsername());
        responseDTO.setUserRole(savedUser.getUserRole());

        return responseDTO;
    }


    @Override
    public ResponseDTO logout(HttpSession session) {
        ResponseDTO responseDTO = new ResponseDTO();

        session.invalidate();
        responseDTO.setMessage("Logout successful");
        responseDTO.setStatus("200");

        return responseDTO;
    }
}