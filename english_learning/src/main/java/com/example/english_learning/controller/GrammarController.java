package com.example.english_learning.controller;

import com.example.english_learning.dto.*;
import com.example.english_learning.service.GrammarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grammar")
public class GrammarController {

    private final GrammarService grammarService;

    public GrammarController(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    // GET /api/grammar?level=A1&q=present
    @GetMapping
    public List<GrammarWithProgressDto> list(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String q
    ) {
        return grammarService.list(level, q);
    }

    // GET /api/grammar/1
    @GetMapping("/{id}")
    public GrammarWithProgressDto getOne(@PathVariable Long id) {
        return grammarService.getOne(id);
    }

    // ---------- Optional admin CRUD ----------
    @PostMapping
    public GrammarWithProgressDto create(@RequestBody UpsertGrammarRequest req) {
        return grammarService.create(req);
    }

    @PutMapping("/{id}")
    public GrammarWithProgressDto update(@PathVariable Long id, @RequestBody UpsertGrammarRequest req) {
        return grammarService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        grammarService.delete(id);
    }

    // ---------- Progress ----------
    @PostMapping("/{id}/known")
    public void setKnown(@PathVariable Long id, @RequestBody SetKnownRequest req) {
        grammarService.setKnown(id, req.known);
    }

    @PostMapping("/{id}/favorite")
    public void setFavorite(@PathVariable Long id, @RequestBody SetFavoriteRequest req) {
        grammarService.setFavorite(id, req.favorite);
    }

    @PostMapping("/{id}/answer")
    public void answer(@PathVariable Long id, @RequestBody AnswerRequest req) {
        grammarService.answer(id, req.correct);
    }
}
