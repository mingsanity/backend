package com.example.english_learning.dto;

public class UserMeResponse {
    public Long id;
    public String email;

    public UserMeResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
