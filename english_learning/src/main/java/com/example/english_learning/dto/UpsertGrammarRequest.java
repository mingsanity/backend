package com.example.english_learning.dto;

import java.util.ArrayList;
import java.util.List;

public class UpsertGrammarRequest {
    public String level;
    public String title;
    public String meaning;

    // content fields (use SAME DTO types as response)
    public String summary;
    public List<String> rules = new ArrayList<>();

    public List<GrammarWithProgressDto.FormRowDto> forms = new ArrayList<>();
    public List<GrammarWithProgressDto.MistakeRowDto> commonMistakes = new ArrayList<>();
    public List<GrammarWithProgressDto.ExampleDto> examples = new ArrayList<>();
    public GrammarWithProgressDto.QuizDto quiz;
}
