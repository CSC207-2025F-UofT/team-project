package interface_adapter.filter_search;

import use_case.filter_search.FilterSearchInputBoundary;
import use_case.filter_search.FilterSearchInputData;

import java.time.LocalDate;

public class FilterSearchController {

    private final FilterSearchInputBoundary interactor;

    public FilterSearchController(FilterSearchInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String category, String start, String end, String merchant) {
        LocalDate startDate = null, endDate = null;

        try { if (!start.isBlank()) startDate = LocalDate.parse(start); }
        catch (Exception ignored) {}

        try { if (!end.isBlank()) endDate = LocalDate.parse(end); }
        catch (Exception ignored) {}

        FilterSearchInputData inputData =
                new FilterSearchInputData(category, startDate, endDate, merchant);

        interactor.execute(inputData);
    }
}
