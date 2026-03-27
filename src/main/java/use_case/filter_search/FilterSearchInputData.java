package use_case.filter_search;

import java.time.LocalDate;

public class FilterSearchInputData {
    private final String category;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String merchant;

    public FilterSearchInputData(String category, LocalDate startDate, LocalDate endDate, String merchant) {
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.merchant = merchant;
    }

    public String getCategory() { return category; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getMerchant() { return merchant; }
}
