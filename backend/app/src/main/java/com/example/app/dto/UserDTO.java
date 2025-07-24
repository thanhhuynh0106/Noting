package com.example.app.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private int id;
    private String fullName;
    private String number;
    private String dob;
    private String email;
    private String userName;
    private String password;
    private Date createdAt;
    private String userRole;
}
