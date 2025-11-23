package entity;

import java.util.List;

public class ChartViewModel {

    private final String title; // "DOW | DAILY Price"
    private final List<String> labels; // X
    private final List<Double> prices; // Y

    public ChartViewModel(String title, List<String> labels, List<Double> prices) {
        this.title = title;
        this.labels = labels;
        this.prices = prices;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Double> getPrices() {
        return prices;
    }
}