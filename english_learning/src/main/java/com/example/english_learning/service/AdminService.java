package com.example.english_learning.service;

import com.example.english_learning.dto.AdminUserDto;
import com.example.english_learning.dto.GrammarSummaryDto;
import com.example.english_learning.dto.ProfileSummaryDto;
import com.example.english_learning.model.User;
import com.example.english_learning.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepo;
    private final ProfileService profileService;

    public AdminService(UserRepository userRepo, ProfileService profileService) {
        this.userRepo = userRepo;
        this.profileService = profileService;
    }

    public List<AdminUserDto> listUsers(String q) {
        List<User> users = (q == null || q.isBlank())
                ? userRepo.findAll()
                : userRepo.findByEmailContainingIgnoreCase(q);

        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public AdminUserDto getUser(Long id) {
        User u = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(u);
    }

    public ProfileSummaryDto getUserSummary(Long userId) {
        return profileService.getSummaryForUser(userId);
    }

    // ✅ NEW
    public GrammarSummaryDto getUserGrammarSummary(Long userId) {
        return profileService.getGrammarSummaryForUser(userId);
    }

    private AdminUserDto toDto(User u) {
        AdminUserDto d = new AdminUserDto();
        d.id = u.getId();
        d.email = u.getEmail();
        d.fullName = u.getFullName();
        d.phone = u.getPhone();
        d.country = u.getCountry();
        d.bio = u.getBio();
        d.role = u.getRole();
        return d;
    }
}
