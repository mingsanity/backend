package com.example.english_learning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grammar")
public class Grammar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 8)
    private String level;   // A1, A2, B1...

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String meaning;

    /**
     * JSON string (TEXT):
     * {
     *   "summary": "...",
     *   "rules": [...],
     *   "forms": [{"subject":"He/She/It","verb":"goes"}],
     *   "commonMistakes": [{"wrong":"She go","right":"She goes","note":"..."}],
     *   "examples":[{"en":"...","vi":"..."}],
     *   "quiz":{"question":"...","options":["..."],"correct":1,"explain":"..."}
     * }
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    public Long getId() { return id; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
