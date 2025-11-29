package interface_adapter.filter_search;

import use_case.filter_search.FilterSearchInputBoundary;
import use_case.filter_search.FilterSearchInputData;

import java.time.LocalDate;

public class FilterSearchController {

    private final FilterSearchInputBoundary interactor;

    public FilterSearchController(FilterSearchInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String searchText,
                        String category,
                        LocalDate startDate,
                        LocalDate endDate,
                        Double minAmount,
                        Double maxAmount) {

        FilterSearchInputData inputData = new FilterSearchInputData(
                searchText,
                category,
                startDate,
                endDate,
                minAmount,
                maxAmount
        );

        interactor.execute(inputData);
    }
}
