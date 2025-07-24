package com.example.app.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String userName;
    private String password;
    private String userRole;
    private  String captcha;

}
