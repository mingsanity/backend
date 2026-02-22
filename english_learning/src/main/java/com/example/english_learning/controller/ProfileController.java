package com.example.english_learning.controller;

import com.example.english_learning.dto.GrammarSummaryDto;
import com.example.english_learning.dto.ProfileSummaryDto;
import com.example.english_learning.dto.VocabularyWithProgressDto;
import com.example.english_learning.service.ProfileService;
import com.example.english_learning.service.VocabularyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final VocabularyService vocabularyService;

    public ProfileController(ProfileService profileService, VocabularyService vocabularyService) {
        this.profileService = profileService;
        this.vocabularyService = vocabularyService;
    }

    @GetMapping("/summary")
    public ProfileSummaryDto summary() {
        return profileService.getMySummary();
    }

    // ✅ NEW: grammar summary for current user (NO ADMIN REQUIRED)
    @GetMapping("/grammar-summary")
    public GrammarSummaryDto grammarSummary() {
        return profileService.getMyGrammarSummary();
    }

    @GetMapping("/progress")
    public List<VocabularyWithProgressDto> myProgress(
            @RequestParam(required = false, name="lessonId") String topic,
            @RequestParam(required = false) String cefr,
            @RequestParam(required = false) String q
    ) {
        return vocabularyService.getAllWithProgress(topic, cefr, q);
    }

    @PostMapping("/reset")
    public void resetMyProgress() {
        profileService.resetMyProgress();
    }
}
