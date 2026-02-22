package com.example.english_learning.dto;

import java.time.Instant;

public class AdminUpdateProgressRequest {
    public Boolean known;
    public Boolean favorite;
    public Integer correctCount;
    public Integer wrongCount;
    public Instant lastReviewedAt;
}
