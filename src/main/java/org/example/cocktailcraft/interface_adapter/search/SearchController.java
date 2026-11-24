package org.example.cocktailcraft.interface_adapter.search;

import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.use_case.search.SearchInputBoundary;
import org.example.cocktailcraft.use_case.search.SearchInputData;

import java.util.Set;

public class SearchController {
    private final SearchInputBoundary searchInteractor;

    public SearchController(SearchInputBoundary searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    public void clear() {
        searchInteractor.clear();
    }

    public void execute(String name, Set<Tags.BaseSpirit> baseSpirits) {
        searchInteractor.execute(new SearchInputData(name, baseSpirits));
    }
}
