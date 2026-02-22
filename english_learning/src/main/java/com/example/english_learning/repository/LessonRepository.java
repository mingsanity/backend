package com.example.english_learning.repository;

import com.example.english_learning.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {}
