package com.example.app.model;

import java.util.Date;

public class User {
    private String userName;
    private String password;
    private String email;
    private String number;
    private String dob;
    private String fullName;

    public User(String userName, String password, String email, String number, String dob, String fullName) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.number = number;
        this.dob = dob;
        this.fullName = fullName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
