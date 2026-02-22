package com.example.english_learning.repository;

import com.example.english_learning.model.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarRepository extends JpaRepository<Grammar, Long> {
}
