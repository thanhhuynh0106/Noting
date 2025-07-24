package com.example.app.dto;

import lombok.Data;

@Data
public class ResponseDTO {
    private String message;
    private String status;

    private String token;
    private String userName;
    private String userRole;


}
