package com.example.english_learning.controller;

import com.example.english_learning.dto.*;
import com.example.english_learning.model.Vocabulary;
import com.example.english_learning.service.VocabularyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vocabulary")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    // ✅ GET /api/vocabulary?lessonId=<topic>&cefr=A1&q=apple
    @GetMapping
    public List<VocabularyWithProgressDto> list(
            @RequestParam(required = false, name="lessonId") String topic,
            @RequestParam(required = false) String cefr,
            @RequestParam(required = false) String q
    ) {
        return vocabularyService.getAllWithProgress(topic, cefr, q);
    }

    @GetMapping("/{id}")
    public VocabularyWithProgressDto one(@PathVariable Long id) {
        return vocabularyService.getOne(id);
    }

    @GetMapping("/topics")
    public List<String> topics() {
        return vocabularyService.getTopics();
    }

    @PostMapping("/{id}/known")
    public void setKnown(@PathVariable Long id, @RequestBody SetKnownRequest req) {
        vocabularyService.setKnown(id, req.known);
    }

    @PostMapping("/{id}/favorite")
    public void setFavorite(@PathVariable Long id, @RequestBody SetFavoriteRequest req) {
        vocabularyService.setFavorite(id, req.favorite);
    }

    @PostMapping("/{id}/answer")
    public void answer(@PathVariable Long id, @RequestBody AnswerRequest req) {
        vocabularyService.answer(id, req.correct);
    }

    // ✅ admin create/update vocab (optional for admin page)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Vocabulary create(@RequestBody UpsertVocabularyRequest req) {
        return vocabularyService.create(req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Vocabulary update(@PathVariable Long id, @RequestBody UpsertVocabularyRequest req) {
        return vocabularyService.update(id, req);
    }
}
