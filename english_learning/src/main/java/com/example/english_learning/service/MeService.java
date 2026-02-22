package com.example.english_learning.service;

import com.example.english_learning.dto.MeDto;
import com.example.english_learning.dto.UpdateMeRequest;
import com.example.english_learning.model.User;
import com.example.english_learning.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class MeService {

    private final UserRepository userRepo;
    private final AuthContext authContext;

    public MeService(UserRepository userRepo, AuthContext authContext) {
        this.userRepo = userRepo;
        this.authContext = authContext;
    }

    public MeDto getMe() {
        Long userId = authContext.userId();
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(u);
    }

    public MeDto updateMe(UpdateMeRequest req) {
        Long userId = authContext.userId();
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // update allowed fields
        u.setFullName(req.fullName);
        u.setPhone(req.phone);
        u.setCountry(req.country);
        u.setBio(req.bio);

        userRepo.save(u);
        return toDto(u);
    }

    private MeDto toDto(User u) {
        MeDto d = new MeDto();
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
