package com.example.english_learning.dto;

import java.util.List;

public class GrammarSummaryDto {
    public long totalGrammar;
    public long knownCount;
    public long favoriteCount;
    public long reviewedCount;
    public long correctTotal;
    public long wrongTotal;

    // group by level (A1/A2/B1/...)
    public List<GroupCountDto> byLevel;
}
