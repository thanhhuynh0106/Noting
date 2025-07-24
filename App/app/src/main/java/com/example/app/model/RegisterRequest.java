package com.example.app.model;

public class RegisterRequest {
    String fullName;
    String number;
    String dob;
    String email;
    String userName;
    String password;
    String userRole;
    String captchaInput;

    public RegisterRequest(String fullName, String number, String dob, String email, String userName, String password, String captchaInput) {
        this.fullName = fullName;
        this.number = number;
        this.dob = dob;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.captchaInput = captchaInput;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
}
