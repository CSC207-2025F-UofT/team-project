package org.example.cocktailcraft.use_case.search;

public interface SearchInputBoundary {
    /** Restores the full catalog without requiring search criteria. */
    void clear();

    void execute(SearchInputData searchInputData);
}
