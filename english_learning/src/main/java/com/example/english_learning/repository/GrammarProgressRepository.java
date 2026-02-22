package com.example.english_learning.repository;

import com.example.english_learning.model.GrammarProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GrammarProgressRepository extends JpaRepository<GrammarProgress, Long> {
    Optional<GrammarProgress> findByUser_IdAndGrammar_Id(Long userId, Long grammarId);
    List<GrammarProgress> findAllByUser_Id(Long userId);
}
