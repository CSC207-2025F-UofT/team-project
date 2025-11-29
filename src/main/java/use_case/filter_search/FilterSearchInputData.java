package use_case.filter_search;

import java.time.LocalDate;

public class FilterSearchInputData {

    private final String searchText;
    private final String category;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Double minAmount;
    private final Double maxAmount;

    public FilterSearchInputData(String searchText,
                                 String category,
                                 LocalDate startDate,
                                 LocalDate endDate,
                                 Double minAmount,
                                 Double maxAmount) {

        this.searchText = searchText;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public String getSearchText() { return searchText; }
    public String getCategory() { return category; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Double getMinAmount() { return minAmount; }
    public Double getMaxAmount() { return maxAmount; }
}
