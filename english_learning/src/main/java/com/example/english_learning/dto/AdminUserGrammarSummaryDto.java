package com.example.english_learning.dto;

import java.util.ArrayList;
import java.util.List;

public class AdminUserGrammarSummaryDto {
    public long totalGrammar;
    public long knownCount;
    public List<BucketDto> byLevel = new ArrayList<>();

    public static class BucketDto {
        public String key;   // A1/A2/B1...
        public long total;
        public long known;

        public BucketDto() {}

        public BucketDto(String key, long total, long known) {
            this.key = key;
            this.total = total;
            this.known = known;
        }
    }
}
