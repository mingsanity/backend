package com.example.english_learning.controller;

import com.example.english_learning.dto.AdminUserDto;
import com.example.english_learning.dto.GrammarSummaryDto;
import com.example.english_learning.dto.ProfileSummaryDto;
import com.example.english_learning.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<AdminUserDto> users(@RequestParam(required = false) String q) {
        return adminService.listUsers(q);
    }

    @GetMapping("/users/{id}")
    public AdminUserDto user(@PathVariable Long id) {
        return adminService.getUser(id);
    }

    @GetMapping("/users/{id}/summary")
    public ProfileSummaryDto summary(@PathVariable Long id) {
        return adminService.getUserSummary(id);
    }

    // ✅ NEW
    @GetMapping("/users/{id}/grammar-summary")
    public GrammarSummaryDto grammarSummary(@PathVariable Long id) {
        return adminService.getUserGrammarSummary(id);
    }
}
