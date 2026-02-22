package com.example.english_learning.dto;

public class CreateVocabularyRequest {
    public String word;
    public String meaning;
    public String example;
    public String pronunciation;
    public String cefr;
    public String pos;
    public Long lessonId; // optional
}
