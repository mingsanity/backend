package com.example.english_learning.service;

import com.example.english_learning.dto.GrammarSummaryDto;
import com.example.english_learning.dto.GroupCountDto;
import com.example.english_learning.dto.ProfileSummaryDto;
import com.example.english_learning.model.Grammar;
import com.example.english_learning.model.GrammarProgress;
import com.example.english_learning.model.Vocabulary;
import com.example.english_learning.model.VocabularyProgress;
import com.example.english_learning.repository.GrammarProgressRepository;
import com.example.english_learning.repository.GrammarRepository;
import com.example.english_learning.repository.VocabularyProgressRepository;
import com.example.english_learning.repository.VocabularyRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyProgressRepository progressRepository;

    // ✅ add grammar repos
    private final GrammarRepository grammarRepository;
    private final GrammarProgressRepository grammarProgressRepository;

    private final AuthContext authContext;

    public ProfileService(
            VocabularyRepository vocabularyRepository,
            VocabularyProgressRepository progressRepository,
            GrammarRepository grammarRepository,
            GrammarProgressRepository grammarProgressRepository,
            AuthContext authContext
    ) {
        this.vocabularyRepository = vocabularyRepository;
        this.progressRepository = progressRepository;
        this.grammarRepository = grammarRepository;
        this.grammarProgressRepository = grammarProgressRepository;
        this.authContext = authContext;
    }

    /** ✅ USER: summary for current logged-in user (VOCAB) */
    public ProfileSummaryDto getMySummary() {
        Long userId = authContext.userId();
        return buildVocabSummaryForUser(userId);
    }

    /** ✅ ADMIN: summary for ANY user (VOCAB) */
    public ProfileSummaryDto getSummaryForUser(Long userId) {
        return buildVocabSummaryForUser(userId);
    }

    /** ✅ USER: grammar summary for current logged-in user */
    public GrammarSummaryDto getMyGrammarSummary() {
        Long userId = authContext.userId();
        return buildGrammarSummaryForUser(userId);
    }

    /** ✅ ADMIN: grammar summary for ANY user */
    public GrammarSummaryDto getGrammarSummaryForUser(Long userId) {
        return buildGrammarSummaryForUser(userId);
    }

    /** ✅ USER: reset current user's vocab progress */
    public void resetMyProgress() {
        Long userId = authContext.userId();
        progressRepository.deleteAllByUserId(userId);
    }

    /* =========================================================
       VOCAB SUMMARY
       ========================================================= */
    private ProfileSummaryDto buildVocabSummaryForUser(Long userId) {
        List<Vocabulary> allVocab = vocabularyRepository.findAll();
        List<VocabularyProgress> progressList = progressRepository.findAllByUserId(userId);

        Map<Long, VocabularyProgress> progressMap = progressList.stream()
                .collect(Collectors.toMap(
                        p -> p.getVocabulary().getId(),
                        p -> p,
                        (a, b) -> a
                ));

        ProfileSummaryDto dto = new ProfileSummaryDto();
        dto.totalWords = allVocab.size();

        long known = 0, fav = 0, reviewed = 0, correctTotal = 0, wrongTotal = 0;

        for (Vocabulary v : allVocab) {
            VocabularyProgress p = progressMap.get(v.getId());
            if (p == null) continue;

            if (p.isKnown()) known++;
            if (p.isFavorite()) fav++;
            if (p.getCorrectCount() > 0 || p.getWrongCount() > 0) reviewed++;

            correctTotal += p.getCorrectCount();
            wrongTotal += p.getWrongCount();
        }

        dto.knownCount = known;
        dto.favoriteCount = fav;
        dto.reviewedCount = reviewed;
        dto.correctTotal = correctTotal;
        dto.wrongTotal = wrongTotal;

        dto.byCefr = buildVocabGroupStats(allVocab, progressMap, v -> safe(v.getCefr(), "UNKNOWN"));
        dto.byTopic = buildVocabGroupStats(allVocab, progressMap, v -> safe(v.getTopic(), "No Topic"));

        return dto;
    }

    private List<GroupCountDto> buildVocabGroupStats(
            List<Vocabulary> allVocab,
            Map<Long, VocabularyProgress> progressMap,
            java.util.function.Function<Vocabulary, String> keyFn
    ) {
        Map<String, List<Vocabulary>> grouped = allVocab.stream()
                .collect(Collectors.groupingBy(keyFn));

        List<GroupCountDto> out = new ArrayList<>();
        for (var e : grouped.entrySet()) {
            String key = e.getKey();
            List<Vocabulary> items = e.getValue();

            long total = items.size();
            long known = 0, fav = 0;

            for (Vocabulary v : items) {
                VocabularyProgress p = progressMap.get(v.getId());
                if (p == null) continue;
                if (p.isKnown()) known++;
                if (p.isFavorite()) fav++;
            }

            out.add(new GroupCountDto(key, total, known, fav));
        }

        out.sort((a, b) -> Long.compare(b.total, a.total));
        return out;
    }

    /* =========================================================
       GRAMMAR SUMMARY
       ========================================================= */
    private GrammarSummaryDto buildGrammarSummaryForUser(Long userId) {
        List<Grammar> all = grammarRepository.findAll();
        List<GrammarProgress> progressList = grammarProgressRepository.findAllByUser_Id(userId);

        Map<Long, GrammarProgress> progressMap = progressList.stream()
                .collect(Collectors.toMap(
                        p -> p.getGrammar().getId(),
                        p -> p,
                        (a, b) -> a
                ));

        GrammarSummaryDto dto = new GrammarSummaryDto();
        dto.totalGrammar = all.size();

        long known = 0, fav = 0, reviewed = 0, correctTotal = 0, wrongTotal = 0;

        for (Grammar g : all) {
            GrammarProgress p = progressMap.get(g.getId());
            if (p == null) continue;

            if (p.isKnown()) known++;
            if (p.isFavorite()) fav++;
            if (p.getCorrectCount() > 0 || p.getWrongCount() > 0) reviewed++;

            correctTotal += p.getCorrectCount();
            wrongTotal += p.getWrongCount();
        }

        dto.knownCount = known;
        dto.favoriteCount = fav;
        dto.reviewedCount = reviewed;
        dto.correctTotal = correctTotal;
        dto.wrongTotal = wrongTotal;

        dto.byLevel = buildGrammarGroupStatsByLevel(all, progressMap);

        return dto;
    }

    private List<GroupCountDto> buildGrammarGroupStatsByLevel(
            List<Grammar> all,
            Map<Long, GrammarProgress> progressMap
    ) {
        Map<String, List<Grammar>> grouped = all.stream()
                .collect(Collectors.groupingBy(g -> safe(g.getLevel(), "UNKNOWN")));

        List<GroupCountDto> out = new ArrayList<>();
        for (var e : grouped.entrySet()) {
            String key = e.getKey();
            List<Grammar> items = e.getValue();

            long total = items.size();
            long known = 0, fav = 0;

            for (Grammar g : items) {
                GrammarProgress p = progressMap.get(g.getId());
                if (p == null) continue;
                if (p.isKnown()) known++;
                if (p.isFavorite()) fav++;
            }

            out.add(new GroupCountDto(key, total, known, fav));
        }

        // ✅ sort by CEFR order
        List<String> order = List.of("A1", "A2", "B1", "B2", "C1", "C2", "UNKNOWN");
        out.sort((a, b) -> {
            String la = safe(a.key, "UNKNOWN").toUpperCase();
            String lb = safe(b.key, "UNKNOWN").toUpperCase();
            int ia = order.indexOf(la);
            int ib = order.indexOf(lb);
            if (ia < 0) ia = 999;
            if (ib < 0) ib = 999;
            int cmp = Integer.compare(ia, ib);
            if (cmp != 0) return cmp;
            return la.compareTo(lb);
        });

        return out;
    }

    private String safe(String s, String fallback) {
        if (s == null) return fallback;
        String t = s.trim();
        return t.isEmpty() ? fallback : t;
    }
}
