package com.example.english_learning.controller;

import com.example.english_learning.dto.MeDto;
import com.example.english_learning.dto.UpdateMeRequest;
import com.example.english_learning.service.MeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final MeService meService;

    public MeController(MeService meService) {
        this.meService = meService;
    }

    @GetMapping
    public MeDto me() {
        return meService.getMe();
    }

    @PutMapping
    public MeDto update(@RequestBody UpdateMeRequest req) {
        return meService.updateMe(req);
    }
}
