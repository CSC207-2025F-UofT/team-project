package org.example.cocktailcraft.interface_adapter.search;

import org.example.cocktailcraft.use_case.search.SearchInputBoundary;
import org.example.cocktailcraft.use_case.search.SearchInputData;

import java.util.List;

public class SearchController {
    private final SearchInputBoundary interactor;

    public SearchController(SearchInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String name, List<String> baseSpirits) {
        interactor.execute(new SearchInputData(name, baseSpirits));
    }
}
