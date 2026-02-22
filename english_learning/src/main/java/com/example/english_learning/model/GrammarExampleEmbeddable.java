package com.example.english_learning.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class GrammarExampleEmbeddable {

    @Column(columnDefinition = "TEXT")
    private String en;

    @Column(columnDefinition = "TEXT")
    private String vi;

    public GrammarExampleEmbeddable() {}

    public GrammarExampleEmbeddable(String en, String vi) {
        this.en = en;
        this.vi = vi;
    }

    public String getEn() { return en; }
    public void setEn(String en) { this.en = en; }

    public String getVi() { return vi; }
    public void setVi(String vi) { this.vi = vi; }
}
