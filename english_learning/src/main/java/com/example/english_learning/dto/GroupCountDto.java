package com.example.english_learning.dto;

public class GroupCountDto {
    public String key;
    public long total;
    public long known;
    public long favorite;

    public GroupCountDto() {}
    public GroupCountDto(String key, long total, long known, long favorite) {
        this.key = key;
        this.total = total;
        this.known = known;
        this.favorite = favorite;
    }
}
