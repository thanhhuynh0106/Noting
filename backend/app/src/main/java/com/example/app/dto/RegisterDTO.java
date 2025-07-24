package com.example.app.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String fullName;
    private String number;
    private String dob;
    private String email;
    private String userName;
    private String password;
    private String userRole;
    private String captcha;
}
