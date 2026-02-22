package com.example.english_learning.dto;

import java.time.Instant;

public class VocabularyWithProgressDto {
    public Long id;
    public String word;
    public String meaning;
    public String example;
    public String pronunciation;

    public String pos;
    public String cefr;
    public String topic;

    // progress
    public boolean known;
    public boolean favorite;
    public int correctCount;
    public int wrongCount;
    public Instant lastReviewedAt;
}
