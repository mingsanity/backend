package com.example.english_learning.dto;

import java.util.List;

public class ProfileSummaryDto {
    public long totalWords;
    public long knownCount;
    public long favoriteCount;
    public long reviewedCount;
    public long correctTotal;
    public long wrongTotal;

    public List<GroupCountDto> byCefr;
    public List<GroupCountDto> byTopic;
}
