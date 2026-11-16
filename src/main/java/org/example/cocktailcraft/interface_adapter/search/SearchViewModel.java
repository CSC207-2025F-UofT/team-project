package org.example.cocktailcraft.interface_adapter.search;

import org.example.cocktailcraft.interface_adapter.ViewModel;

public class SearchViewModel extends ViewModel<SearchState> {
    public SearchViewModel() {
        super(new SearchState());
    }
}
