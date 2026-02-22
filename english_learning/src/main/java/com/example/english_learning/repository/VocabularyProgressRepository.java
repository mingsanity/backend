package com.example.english_learning.repository;

import com.example.english_learning.model.VocabularyProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VocabularyProgressRepository extends JpaRepository<VocabularyProgress, Long> {

    Optional<VocabularyProgress> findByUserIdAndVocabularyId(Long userId, Long vocabularyId);

    List<VocabularyProgress> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
