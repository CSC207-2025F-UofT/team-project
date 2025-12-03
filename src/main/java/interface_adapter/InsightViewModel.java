package interface_adapter;

public class InsightViewModel {

    private String summary;
    private String recommendations;
    private String date;
    private String spendingBreakdown; // NEW

    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpendingBreakdown() {
        return spendingBreakdown;
    }

    public void setSpendingBreakdown(String spendingBreakdown) {
        this.spendingBreakdown = spendingBreakdown;
    }
}
