package org.example.cocktailcraft.app;

import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.data_access.SearchCocktailDataAccessObject;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.interface_adapter.click.ClickController;
import org.example.cocktailcraft.interface_adapter.click.ClickPresenter;
import org.example.cocktailcraft.interface_adapter.click.ClickViewModel;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchPresenter;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.use_case.click.ClickDataAccessInterface;
import org.example.cocktailcraft.use_case.click.ClickInputBoundary;
import org.example.cocktailcraft.use_case.click.ClickInteractor;
import org.example.cocktailcraft.use_case.click.ClickOutputBoundary;
import org.example.cocktailcraft.use_case.search.SearchDataAccessInterface;
import org.example.cocktailcraft.use_case.search.SearchInputBoundary;
import org.example.cocktailcraft.use_case.search.SearchInteractor;
import org.example.cocktailcraft.use_case.search.SearchOutputBoundary;
import org.example.cocktailcraft.view.browse.MainPageView;
import org.example.cocktailcraft.view.click.ClickView;
import org.example.cocktailcraft.view.search.SearchView;
import org.example.cocktailcraft.data_access.FileCustomRecipeDataAccessObject;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeController;
import org.example.cocktailcraft.interface_adapter.customize.CustomizePresenter;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeViewModel;
import org.example.cocktailcraft.use_case.customize.CustomizeInteractor;
import org.example.cocktailcraft.view.customize.CustomizeView;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeState;

import java.util.List;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class AppBuilder {
    private SearchViewModel searchViewModel;
    private SearchView searchView;
    private MainPageView mainPageView;
    private List<Cocktail> cocktails;
    private ClickViewModel clickViewModel;
    private ClickView clickView;
    private ClickController clickController;
    private CustomizeViewModel customizeViewModel;
    private CustomizeView customizeView;

    public AppBuilder addSearchView() {
        searchViewModel = new SearchViewModel();
        searchView = new SearchView(searchViewModel);
        return this;
    }

    public AppBuilder addSearchUseCase() {
        return addSearchUseCase(new SearchCocktailDataAccessObject(new CocktailJsonReader()));
    }

    public AppBuilder addSearchUseCase(SearchDataAccessInterface searchDataAccessObject) {
        final SearchOutputBoundary searchOutputBoundary =
                new SearchPresenter(searchViewModel);

        final SearchInputBoundary searchInteractor =
                new SearchInteractor(searchDataAccessObject, searchOutputBoundary);

        final SearchController searchController =
                new SearchController(searchInteractor);

        searchView.setSearchController(searchController);
        searchController.clear();
        cocktails = searchViewModel.getState().getResults();
        return this;
    }

    public AppBuilder addClickUseCase() {
        clickViewModel = new ClickViewModel();
        clickView = new ClickView(clickViewModel);

        final ClickDataAccessInterface clickDataAccessObject =
                new CocktailJsonReader();

        final ClickOutputBoundary clickOutputBoundary =
                new ClickPresenter(clickViewModel);

        final ClickInputBoundary clickInteractor =
                new ClickInteractor(clickDataAccessObject, clickOutputBoundary);

        clickController = new ClickController(clickInteractor);

        return this;
    }

    public AppBuilder addMainPageView() {
        mainPageView = new MainPageView(searchView,
                cocktails,
                clickController,
                clickView,
                searchViewModel);
        return this;
    }

    public AppBuilder addCustomizeUseCase(Supplier<String> currentUserId) {
        customizeViewModel = new CustomizeViewModel();

        final FileCustomRecipeDataAccessObject dataAccess =
                new FileCustomRecipeDataAccessObject(
                        Path.of("data", "custom-recipes.json")
                );

        final CustomizePresenter presenter =
                new CustomizePresenter(customizeViewModel);

        final CustomizeInteractor interactor =
                new CustomizeInteractor(dataAccess, presenter);

        final CustomizeController controller =
                new CustomizeController(interactor);

        customizeView = new CustomizeView(
                customizeViewModel,
                controller,
                currentUserId
        );

        return this;
    }

    public JFrame build() {
        final JFrame frame = new JFrame("CocktailCraft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final CardLayout cardLayout = new CardLayout();
        final JPanel pages = new JPanel(cardLayout);

        pages.add(mainPageView, "home");
        pages.add(customizeView, "customize");

        mainPageView.setCustomizeAction(cocktail -> {
            CustomizeState state = new CustomizeState();

            state.setOriginalCocktailId(cocktail.getId());
            state.setName(cocktail.getName());
            state.setIngredients(cocktail.getIngredients());
            state.setInstructions(cocktail.getInstructions());

            customizeViewModel.setState(state);
            customizeViewModel.notifyStateChanged();

            cardLayout.show(pages, "customize");
        });

        customizeView.setCancelAction(() -> {
            customizeViewModel.setState(new CustomizeState());
            customizeViewModel.notifyStateChanged();

            cardLayout.show(pages, "home");
        });

        frame.setContentPane(pages);
        cardLayout.show(pages, "home");

        return frame;
    }
}
