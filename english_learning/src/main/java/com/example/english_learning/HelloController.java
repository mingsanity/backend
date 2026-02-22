package com.example.english_learning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
}
