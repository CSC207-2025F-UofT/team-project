package com.studyarc.entity;

import java.time.LocalDate;

public class Reflection {
    private final String contents;
    private final LocalDate date;

    public Reflection(String contents, LocalDate date) {
        this.contents = contents;
        this.date = date;
    }

    public String getContents() {
        return contents;
    }

    public LocalDate getDate() {
        return date;
    }
}
