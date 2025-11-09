package com.studyarc.use_case.reflection_log;
import java.time.LocalDate;

public class ReflectionLogInputData {
    private final String milestone;
    private final String contents;
    private final LocalDate date;

    public ReflectionLogInputData(String milestone, String contents, LocalDate date) {
        this.milestone = milestone;
        this.contents = contents;
        this.date = date;
    }

    public String getMilestone() {
        return milestone;
    }

    public String getContents() {
        return contents;
    }

    public LocalDate getDate() {
        return date;
    }

}
