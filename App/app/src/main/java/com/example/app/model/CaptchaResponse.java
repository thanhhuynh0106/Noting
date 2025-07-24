package com.example.app.model;

public class CaptchaResponse {
    private String image;

    public String getImage() {
        return image;
    }

    public String solvedText;

    public String getSolvedText() {
        return solvedText;
    }

    public void setSolvedText(String solvedText) {
        this.solvedText = solvedText;
    }

    public void setImage(String image) {
        this.image = image;
    }
}