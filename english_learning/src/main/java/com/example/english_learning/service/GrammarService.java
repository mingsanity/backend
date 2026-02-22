package com.example.english_learning.service;

import com.example.english_learning.dto.GrammarWithProgressDto;
import com.example.english_learning.dto.UpsertGrammarRequest;
import com.example.english_learning.model.Grammar;
import com.example.english_learning.model.GrammarProgress;
import com.example.english_learning.model.User;
import com.example.english_learning.repository.GrammarProgressRepository;
import com.example.english_learning.repository.GrammarRepository;
import com.example.english_learning.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GrammarService {

    private final GrammarRepository grammarRepository;
    private final GrammarProgressRepository grammarProgressRepository;
    private final UserRepository userRepository;
    private final AuthContext authContext;
    private final ObjectMapper objectMapper;

    public GrammarService(
            GrammarRepository grammarRepository,
            GrammarProgressRepository grammarProgressRepository,
            UserRepository userRepository,
            AuthContext authContext,
            ObjectMapper objectMapper
    ) {
        this.grammarRepository = grammarRepository;
        this.grammarProgressRepository = grammarProgressRepository;
        this.userRepository = userRepository;
        this.authContext = authContext;
        this.objectMapper = objectMapper;
    }

    public List<GrammarWithProgressDto> list(String level, String q) {
        Long userId = authContext.userId();

        List<Grammar> all = grammarRepository.findAll();

        if (level != null && !level.isBlank() && !"all".equalsIgnoreCase(level)) {
            String lv = level.trim();
            all = all.stream()
                    .filter(g -> g.getLevel() != null && g.getLevel().equalsIgnoreCase(lv))
                    .toList();
        }

        if (q != null && !q.isBlank()) {
            String qq = q.trim().toLowerCase();
            all = all.stream()
                    .filter(g ->
                            (g.getTitle() != null && g.getTitle().toLowerCase().contains(qq)) ||
                                    (g.getMeaning() != null && g.getMeaning().toLowerCase().contains(qq))
                    )
                    .toList();
        }

        // ✅ FIX: repository method must be findAllByUser_Id
        Map<Long, GrammarProgress> pmap = grammarProgressRepository.findAllByUser_Id(userId).stream()
                .collect(Collectors.toMap(p -> p.getGrammar().getId(), p -> p));

        List<GrammarWithProgressDto> out = new ArrayList<>();
        for (Grammar g : all) {
            out.add(toDto(g, pmap.get(g.getId())));
        }
        return out;
    }

    public GrammarWithProgressDto getOne(Long id) {
        Long userId = authContext.userId();
        Grammar g = grammarRepository.findById(id).orElseThrow(() -> new RuntimeException("Grammar not found"));

        // ✅ FIX: repository method must be findByUser_IdAndGrammar_Id
        GrammarProgress p = grammarProgressRepository.findByUser_IdAndGrammar_Id(userId, id).orElse(null);

        return toDto(g, p);
    }

    public GrammarWithProgressDto create(UpsertGrammarRequest req) {
        Grammar g = new Grammar();
        applyUpsert(g, req);
        Grammar saved = grammarRepository.save(g);
        return toDto(saved, null);
    }

    public GrammarWithProgressDto update(Long id, UpsertGrammarRequest req) {
        Long userId = authContext.userId();
        Grammar g = grammarRepository.findById(id).orElseThrow(() -> new RuntimeException("Grammar not found"));
        applyUpsert(g, req);
        Grammar saved = grammarRepository.save(g);

        // ✅ FIX
        GrammarProgress p = grammarProgressRepository.findByUser_IdAndGrammar_Id(userId, id).orElse(null);

        return toDto(saved, p);
    }

    public void delete(Long id) {
        grammarRepository.deleteById(id);
    }

    // progress endpoints
    public void setKnown(Long grammarId, boolean known) {
        GrammarProgress p = getOrCreateProgress(grammarId);
        p.setKnown(known);
        p.setLastReviewedAt(Instant.now());
        grammarProgressRepository.save(p);
    }

    public void setFavorite(Long grammarId, boolean favorite) {
        GrammarProgress p = getOrCreateProgress(grammarId);
        p.setFavorite(favorite);
        grammarProgressRepository.save(p);
    }

    public void answer(Long grammarId, boolean correct) {
        GrammarProgress p = getOrCreateProgress(grammarId);
        if (correct) p.setCorrectCount(p.getCorrectCount() + 1);
        else p.setWrongCount(p.getWrongCount() + 1);
        p.setLastReviewedAt(Instant.now());
        grammarProgressRepository.save(p);
    }

    // ---------------- Helpers ----------------

    private GrammarProgress getOrCreateProgress(Long grammarId) {
        Long userId = authContext.userId();
        Grammar g = grammarRepository.findById(grammarId)
                .orElseThrow(() -> new RuntimeException("Grammar not found"));

        // ✅ FIX
        return grammarProgressRepository.findByUser_IdAndGrammar_Id(userId, grammarId)
                .orElseGet(() -> {
                    User u = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    GrammarProgress p = new GrammarProgress();
                    p.setUser(u);
                    p.setGrammar(g);
                    return p;
                });
    }

    private GrammarWithProgressDto toDto(Grammar g, GrammarProgress p) {
        GrammarWithProgressDto dto = new GrammarWithProgressDto();
        dto.id = g.getId();
        dto.level = g.getLevel();
        dto.title = g.getTitle();
        dto.meaning = g.getMeaning();

        ContentPayload payload = parseContent(g.getContent());
        dto.summary = payload.summary;
        dto.rules = payload.rules;
        dto.forms = payload.forms;
        dto.commonMistakes = payload.commonMistakes;
        dto.examples = payload.examples;
        dto.quiz = payload.quiz;

        if (p != null) {
            dto.known = p.isKnown();
            dto.favorite = p.isFavorite();
            dto.correctCount = p.getCorrectCount();
            dto.wrongCount = p.getWrongCount();
            dto.lastReviewedAt = p.getLastReviewedAt();
        }
        return dto;
    }

    private ContentPayload parseContent(String json) {
        ContentPayload empty = new ContentPayload();
        empty.summary = "";
        empty.rules = new ArrayList<>();
        empty.forms = new ArrayList<>();
        empty.commonMistakes = new ArrayList<>();
        empty.examples = new ArrayList<>();
        empty.quiz = null;

        if (json == null || json.isBlank()) return empty;

        try {
            ContentPayload p = objectMapper.readValue(json, ContentPayload.class);
            if (p.rules == null) p.rules = new ArrayList<>();
            if (p.forms == null) p.forms = new ArrayList<>();
            if (p.commonMistakes == null) p.commonMistakes = new ArrayList<>();
            if (p.examples == null) p.examples = new ArrayList<>();
            return p;
        } catch (Exception ignored) {
            return empty;
        }
    }

    private void applyUpsert(Grammar g, UpsertGrammarRequest req) {
        if (req == null) throw new RuntimeException("Body is required");
        if (req.level == null || req.level.isBlank()) throw new RuntimeException("level is required");
        if (req.title == null || req.title.isBlank()) throw new RuntimeException("title is required");

        g.setLevel(req.level.trim());
        g.setTitle(req.title.trim());
        g.setMeaning(req.meaning);

        ContentPayload payload = new ContentPayload();
        payload.summary = (req.summary == null) ? "" : req.summary;
        payload.rules = (req.rules == null) ? new ArrayList<>() : req.rules;

        payload.forms = (req.forms == null) ? new ArrayList<>() : req.forms;
        payload.commonMistakes = (req.commonMistakes == null) ? new ArrayList<>() : req.commonMistakes;
        payload.examples = (req.examples == null) ? new ArrayList<>() : req.examples;
        payload.quiz = req.quiz;

        try {
            g.setContent(objectMapper.writeValueAsString(payload));
        } catch (Exception e) {
            throw new RuntimeException("Invalid content payload");
        }
    }

    // Stored in Grammar.content (JSON)
    public static class ContentPayload {
        public String summary;
        public List<String> rules;

        public List<GrammarWithProgressDto.FormRowDto> forms;
        public List<GrammarWithProgressDto.MistakeRowDto> commonMistakes;
        public List<GrammarWithProgressDto.ExampleDto> examples;
        public GrammarWithProgressDto.QuizDto quiz;
    }
}
