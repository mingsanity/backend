package com.example.english_learning.repository;

import com.example.english_learning.model.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    // topic = lessonId param from frontend (string topic)
    @Query("""
        select v from Vocabulary v
        where (:topic is null or :topic = '' or v.topic = :topic)
          and (:cefr is null or :cefr = '' or v.cefr = :cefr)
          and (
              :q is null or :q = '' 
              or lower(v.word) like lower(concat('%', :q, '%'))
              or lower(v.meaning) like lower(concat('%', :q, '%'))
              or lower(coalesce(v.example,'')) like lower(concat('%', :q, '%'))
              or lower(coalesce(v.pos,'')) like lower(concat('%', :q, '%'))
          )
        order by v.id asc
    """)
    List<Vocabulary> search(@Param("topic") String topic,
                            @Param("cefr") String cefr,
                            @Param("q") String q);

    @Query("""
        select distinct v.topic from Vocabulary v
        where v.topic is not null and v.topic <> ''
        order by v.topic asc
    """)
    List<String> findAllTopics();
}
