package com.studyarc.entity.job_postings;

import java.util.List;

public class KeywordList {
    private final List<String> keywords;

    public KeywordList(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
