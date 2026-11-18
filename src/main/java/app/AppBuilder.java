package app;
import java.nio.file.Paths;
import javax.swing.JFrame;

//Access interfaces
import use_case.recipe.*;
import use_case.favorite_recipe.*;

//Data Access Implementations
import data_access.*;

//Use Cases
import use_case.search_recipe.*;
import use_case.view_recipe.*;
import use_case.filter_recipe.*;

//Interface Adapters
import interface_adapter.search_recipe.*;
import interface_adapter.view_recipe.*;
import interface_adapter.filter_recipe.*;
import interface_adapter.favorite_recipe.*;

//GUI
import view.*;

public class AppBuilder {
    //Data Access Objects
    private final RecipeDataAccessInterface recipeDAO;
    private final FavoriteRecipeDataAccessInterface favoriteDAO;

    //Main window
    private final MainFrame mainFrame;

    //View models
    private final SearchRecipeViewModel searchViewModel;
    private final ViewRecipeViewModel viewRecipeViewModel;
    private final FilterRecipeViewModel filterViewModel;
    private final FavoriteRecipeViewModel favoriteViewModel;

    //Views
    private SearchView searchView;
    private RecipeDetailView recipeDetailView; //"Viewview"
    private FilterView filterView;
    private FavoritesView favoritesView;

    //Controllers
    private ViewRecipeController viewRecipeController;
    private FavoriteRecipeController favoriteRecipeController;

    //Constructor
    public AppBuilder() {
        this.recipeDao = new MealDbRecipeDataAccessObject();
        this.favoriteDAO = new FileFavoriteDataAccessObject();

        this.mainFrame = new MainFrame();
        this.searchViewModel = new SearchRecipeViewModel();
        this.viewRecipeViewModel = new ViewRecipeViewModel();
        this.filterViewModel = new FilterRecipeViewModel();
        this.favoriteViewModel = new FavoriteRecipeViewModel();
    }

    //Use Case 1: search
    public AppBuilder addSearchRecipeUseCase() {
        SearchRecipePresenter presenter = new SearchRecipePresenter(searchViewModel);
        SearchRecipeInputBoundary interacter = new SearchRecipeInteractor(recipeDAO, presenter);
        SearchRecipeController controller = new SearchRecipeController(interacter);
        searchView = new SearchView(controller, searchViewModel);
        mainFrame.addView("Search", searchView); //TODO
        return this;
    }

    //Use Case 2: View
    public AppBuilder addViewRecipeUseCase() {
        ViewRecipePresenter presenter = new ViewRecipePresenter(ViewRecipeViewModel);
        ViewRecipeInputBoundary interacter = new ViewRecipeInteractor(recipeDAO, presenter);
        ViewRecipeController controller = new ViewRecipeController(interacter);
        recipeDetailView = new RecipeDetailView(viewRecipeController, viewRecipeViewModel);
        mainFrame.addView("Details", recipeDetailView); //TODO
        return this;
    }

    //Use Case 3: Filter
    public AppBuilder addFilterRecipeUseCase() {
        FilterRecipePresenter presenter = new FilterRecipePresenter(FilterRecipeViewModel);
        FilterRecipeInputBoundary interacter = new FilterRecipeInteractor(recipeDAO, presenter);
        ViewRecipeController controller = new FilterRecipeController(interacter);
        filterView = new FilterView(controller, filterViewModel);
        mainFrame.addView("Filter", filterView); //TODO
        return this;
    }

    //Use Case 4: Favorite
    public AppBuilder addFavoriteRecipeUseCase() {
        FavoriteRecipePresenter presenter = new FavoriteRecipePresenter(FavoriteRecipeViewModel);
        FavoriteRecipeInputBoundary interacter = new FavoriteRecipeInteractor(recipeDAO, presenter);
        FavoriteRecipeController controller = new FavoriteRecipeController(interacter);
        favoritesView = new FavoritesView(favoriteRecipeController, favoriteViewModel);
        mainFrame.addView("Favorites", favoritesView); //TODO
        return this;
    }

    //JFrame
    public JFrame build(){
        if (searchView == null || recipeDetailView == null || filterView == null || favoritesView == null){
            throw new IllegalStateException("All four use cases must be added before build()");
    }
        //TODO


    }
}
