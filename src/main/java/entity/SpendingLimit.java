package entity;

public class SpendingLimit {
    private String category;
    private double limit;

    public SpendingLimit(String category, double limit) {
        this.category = category;
        this.limit = limit;
    }
    public String getCategory() {return category;}
    public double getLimit() {return limit;}
}
