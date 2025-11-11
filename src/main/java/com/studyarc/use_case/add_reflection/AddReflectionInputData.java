package com.studyarc.use_case.add_reflection;
import java.time.LocalDate;

public class AddReflectionInputData {
    private final String planName;
    private final String contents;
    private final LocalDate date;

    public AddReflectionInputData(String milestone, String contents, LocalDate date) {
        this.planName = milestone;
        this.contents = contents;
        this.date = date;
    }

    public String getPlanName() {
        return planName;
    }

    public String getContents() {
        return contents;
    }

    public LocalDate getDate() {
        return date;
    }

}
