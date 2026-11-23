package org.example.cocktailcraft.app;

import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.data_access.SearchCocktailDataAccessObject;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchPresenter;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.use_case.search.SearchDataAccessInterface;
import org.example.cocktailcraft.use_case.search.SearchInputBoundary;
import org.example.cocktailcraft.use_case.search.SearchInteractor;
import org.example.cocktailcraft.use_case.search.SearchOutputBoundary;
import org.example.cocktailcraft.view.browse.MainPageView;
import org.example.cocktailcraft.view.search.SearchView;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import javax.swing.JFrame;

public class AppBuilder {
    private SearchViewModel searchViewModel;
    private SearchView searchView;
    private MainPageView mainPageView;

    public AppBuilder addSearchView() {
        searchViewModel = new SearchViewModel();
        searchView = new SearchView(searchViewModel);
        return this;
    }

    public AppBuilder addSearchUseCase() {
        final List<Cocktail> cocktails;
        try {
            cocktails = new CocktailJsonReader().readAll();
        }
        catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }

        final SearchDataAccessInterface searchDataAccessObject =
                new SearchCocktailDataAccessObject(cocktails);

        final SearchOutputBoundary searchOutputBoundary =
                new SearchPresenter(searchViewModel);

        final SearchInputBoundary searchInteractor =
                new SearchInteractor(searchDataAccessObject, searchOutputBoundary);

        final SearchController searchController =
                new SearchController(searchInteractor);

        searchView.setSearchController(searchController);
        return this;
    }

    public AppBuilder addMainPageView() {
        mainPageView = new MainPageView(searchView, searchViewModel);
        return this;
    }

    public JFrame build() {
        final JFrame frame = new JFrame("CocktailCraft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPageView);
        return frame;
    }
}