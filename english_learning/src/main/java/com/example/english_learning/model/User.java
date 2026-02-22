package com.example.english_learning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ✅ role column in DB (USER / ADMIN)
    @Column(nullable = false)
    private String role = "USER";

    // ✅ personal info
    @Column(name = "full_name")
    private String fullName;

    private String phone;
    private String country;

    @Column(columnDefinition = "TEXT")
    private String bio;

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getCountry() { return country; }
    public String getBio() { return bio; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setCountry(String country) { this.country = country; }
    public void setBio(String bio) { this.bio = bio; }
}
