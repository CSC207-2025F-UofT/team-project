package entity;

import java.time.LocalDate;
import java.util.List;

public class Insight {

    private LocalDate generatedAt;
    private String summaryText;
    private List<String> recommendations;

    public LocalDate getGeneratedAt() {
        return this.generatedAt;
    }

    public void setGeneratedAt(LocalDate generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getSummaryText() {
        return this.summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public List<String> getRecommendations() {
        return this.recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }
}
