package com.example.english_learning.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GrammarWithProgressDto {
    public Long id;
    public String level;
    public String title;
    public String meaning;

    // parsed from content JSON
    public String summary;
    public List<String> rules = new ArrayList<>();
    public List<FormRowDto> forms = new ArrayList<>();
    public List<MistakeRowDto> commonMistakes = new ArrayList<>();
    public List<ExampleDto> examples = new ArrayList<>();
    public QuizDto quiz; // can be null

    // progress
    public boolean known;
    public boolean favorite;
    public int correctCount;
    public int wrongCount;
    public Instant lastReviewedAt;

    public static class ExampleDto {
        public String en;
        public String vi;
    }

    public static class QuizDto {
        public String question;
        public List<String> options = new ArrayList<>();
        public Integer correct;
        public String explain; // ✅ important for your UI
    }

    public static class FormRowDto {
        public String subject;
        public String verb;
    }

    public static class MistakeRowDto {
        public String wrong;
        public String right;
        public String note;
    }
}
