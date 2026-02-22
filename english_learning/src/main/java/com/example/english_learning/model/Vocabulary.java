package com.example.english_learning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vocabulary")
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String meaning;

    @Column(columnDefinition = "TEXT")
    private String example;

    private String pronunciation;

    private String pos;

    private String cefr; // A1..C2

    private String topic; // your "lessonId" dropdown label in UI

    public Long getId() { return id; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public String getPronunciation() { return pronunciation; }
    public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }

    public String getPos() { return pos; }
    public void setPos(String pos) { this.pos = pos; }

    public String getCefr() { return cefr; }
    public void setCefr(String cefr) { this.cefr = cefr; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
}
