package use_case.filter_search;

import entity.Transaction;
import java.util.List;

public class FilterSearchInteractor implements FilterSearchInputBoundary {

    private final FilterSearchOutputBoundary presenter;
    private final FilterSearchUserDataAccessInterface dataAccess;
    private final FilterSearchCategoryDataAccessInterface categoryAccess;

    public FilterSearchInteractor(FilterSearchOutputBoundary presenter,
                                  FilterSearchUserDataAccessInterface dataAccess,
                                  FilterSearchCategoryDataAccessInterface categoryAccess) {

        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.categoryAccess = categoryAccess;
    }

    @Override
    public void execute(FilterSearchInputData input) {

        // 1. Validate search text
        if (input.getSearchText() == null || input.getSearchText().isEmpty()) {
            presenter.presentError("Search text cannot be empty.");
            return;
        }

        // 2. Validate category
        if (input.getCategory() != null &&
                !input.getCategory().isEmpty() &&
                !categoryAccess.categoryExists(input.getCategory())) {

            presenter.presentError("Invalid category: " + input.getCategory());
            return;
        }

        // 3. Perform search through repository
        List<Transaction> results = dataAccess.search(
                input.getSearchText(),
                input.getCategory(),
                input.getStartDate(),
                input.getEndDate(),
                input.getMinAmount(),
                input.getMaxAmount()
        );

        // 4. Present results
        presenter.present(new FilterSearchOutputData(results));
    }
}
