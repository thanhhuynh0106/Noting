package com.example.app.model;

public class LoginRequest {
    String userName;
    String password;
    String userRole;
    String captcha;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public LoginRequest(String userName, String password, String userRole, String captcha) {
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
        this.captcha = captcha;
    }
}
