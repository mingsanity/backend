package com.example.english_learning.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "vocabulary_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "vocabulary_id"})
)
public class VocabularyProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id")
    private Vocabulary vocabulary;

    @Column(nullable = false)
    private boolean known = false;

    @Column(nullable = false)
    private boolean favorite = false;

    @Column(nullable = false)
    private int correctCount = 0;

    @Column(nullable = false)
    private int wrongCount = 0;

    private Instant lastReviewedAt;

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Vocabulary getVocabulary() { return vocabulary; }
    public void setVocabulary(Vocabulary vocabulary) { this.vocabulary = vocabulary; }

    public boolean isKnown() { return known; }
    public void setKnown(boolean known) { this.known = known; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public int getCorrectCount() { return correctCount; }
    public void setCorrectCount(int correctCount) { this.correctCount = correctCount; }

    public int getWrongCount() { return wrongCount; }
    public void setWrongCount(int wrongCount) { this.wrongCount = wrongCount; }

    public Instant getLastReviewedAt() { return lastReviewedAt; }
    public void setLastReviewedAt(Instant lastReviewedAt) { this.lastReviewedAt = lastReviewedAt; }
}
