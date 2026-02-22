package com.example.english_learning.service;

import com.example.english_learning.dto.*;
import com.example.english_learning.model.User;
import com.example.english_learning.model.Vocabulary;
import com.example.english_learning.model.VocabularyProgress;
import com.example.english_learning.repository.UserRepository;
import com.example.english_learning.repository.VocabularyProgressRepository;
import com.example.english_learning.repository.VocabularyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final AuthContext authContext;

    public VocabularyService(
            VocabularyRepository vocabularyRepository,
            VocabularyProgressRepository progressRepository,
            UserRepository userRepository,
            AuthContext authContext
    ) {
        this.vocabularyRepository = vocabularyRepository;
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.authContext = authContext;
    }

    // ✅ topic is received via request param name "lessonId" (string)
    public List<VocabularyWithProgressDto> getAllWithProgress(String topic, String cefr, String q) {
        Long userId = authContext.userId();

        List<Vocabulary> vocabList = vocabularyRepository.search(topic, cefr, q);

        List<VocabularyProgress> progressList = progressRepository.findAllByUserId(userId);
        Map<Long, VocabularyProgress> progressMap = progressList.stream()
                .collect(Collectors.toMap(p -> p.getVocabulary().getId(), p -> p));

        return vocabList.stream().map(v -> toDto(v, progressMap.get(v.getId()))).toList();
    }

    public VocabularyWithProgressDto getOne(Long vocabId) {
        Long userId = authContext.userId();

        Vocabulary v = vocabularyRepository.findById(vocabId)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));

        VocabularyProgress p = progressRepository.findByUserIdAndVocabularyId(userId, vocabId).orElse(null);
        return toDto(v, p);
    }

    public void setKnown(Long vocabId, boolean known) {
        VocabularyProgress p = getOrCreateProgress(authContext.userId(), vocabId);
        p.setKnown(known);
        p.setLastReviewedAt(Instant.now());
        progressRepository.save(p);
    }

    public void setFavorite(Long vocabId, boolean favorite) {
        VocabularyProgress p = getOrCreateProgress(authContext.userId(), vocabId);
        p.setFavorite(favorite);
        progressRepository.save(p);
    }

    public void answer(Long vocabId, boolean correct) {
        VocabularyProgress p = getOrCreateProgress(authContext.userId(), vocabId);
        if (correct) p.setCorrectCount(p.getCorrectCount() + 1);
        else p.setWrongCount(p.getWrongCount() + 1);
        p.setLastReviewedAt(Instant.now());
        progressRepository.save(p);
    }

    // ✅ admin create/update vocab helper
    public Vocabulary create(UpsertVocabularyRequest req) {
        if (!authContext.isAdmin()) throw new RuntimeException("Forbidden");

        Vocabulary v = new Vocabulary();
        applyUpsert(v, req);
        return vocabularyRepository.save(v);
    }

    public Vocabulary update(Long id, UpsertVocabularyRequest req) {
        if (!authContext.isAdmin()) throw new RuntimeException("Forbidden");

        Vocabulary v = vocabularyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));
        applyUpsert(v, req);
        return vocabularyRepository.save(v);
    }

    public List<String> getTopics() {
        return vocabularyRepository.findAllTopics();
    }

    private void applyUpsert(Vocabulary v, UpsertVocabularyRequest req) {
        if (req.word == null || req.word.isBlank()) throw new RuntimeException("word required");
        if (req.meaning == null || req.meaning.isBlank()) throw new RuntimeException("meaning required");

        v.setWord(req.word.trim());
        v.setMeaning(req.meaning.trim());
        v.setExample(req.example);
        v.setPronunciation(req.pronunciation);
        v.setPos(req.pos);
        v.setCefr(req.cefr);
        v.setTopic(req.topic);
    }

    private VocabularyProgress getOrCreateProgress(Long userId, Long vocabId) {
        Vocabulary v = vocabularyRepository.findById(vocabId)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));

        return progressRepository.findByUserIdAndVocabularyId(userId, vocabId)
                .orElseGet(() -> {
                    User u = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    VocabularyProgress p = new VocabularyProgress();
                    p.setUser(u);
                    p.setVocabulary(v);
                    p.setKnown(false);
                    p.setFavorite(false);
                    p.setCorrectCount(0);
                    p.setWrongCount(0);
                    p.setLastReviewedAt(null);
                    return p;
                });
    }

    private VocabularyWithProgressDto toDto(Vocabulary v, VocabularyProgress p) {
        VocabularyWithProgressDto dto = new VocabularyWithProgressDto();
        dto.id = v.getId();
        dto.word = v.getWord();
        dto.meaning = v.getMeaning();
        dto.example = v.getExample();
        dto.pronunciation = v.getPronunciation();

        dto.pos = v.getPos();
        dto.cefr = v.getCefr();
        dto.topic = v.getTopic();

        if (p != null) {
            dto.known = p.isKnown();
            dto.favorite = p.isFavorite();
            dto.correctCount = p.getCorrectCount();
            dto.wrongCount = p.getWrongCount();
            dto.lastReviewedAt = p.getLastReviewedAt();
        } else {
            dto.known = false;
            dto.favorite = false;
            dto.correctCount = 0;
            dto.wrongCount = 0;
            dto.lastReviewedAt = null;
        }
        return dto;
    }
}
