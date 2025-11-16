package test;

import data_access.FileFavoriteLocationsGateway;
import interface_adapters.save_favorite_loc.FavoriteLocationsViewModel;
import interface_adapters.save_favorite_loc.FavoriteLocationsController;
import interface_adapters.save_favorite_loc.SaveFavoriteLocationPresenter;
import use_case.save_favorite.SaveFavoriteLocationInteractor;

public class SaveFavoriteLocationDemo {

    public static void main(String[] args) {
        // 1. Gateway
        FileFavoriteLocationsGateway gateway =
                new FileFavoriteLocationsGateway("favorites_test.txt");

        // 2. ViewModel + Presenter
        FavoriteLocationsViewModel viewModel = new FavoriteLocationsViewModel();
        SaveFavoriteLocationPresenter presenter =
                new SaveFavoriteLocationPresenter(viewModel);

        // 3. Interactor
        SaveFavoriteLocationInteractor interactor =
                new SaveFavoriteLocationInteractor(gateway, presenter);

        // 4. Controller
        FavoriteLocationsController controller =
                new FavoriteLocationsController(interactor);

        // ---- TEST RUNS ----

        System.out.println("Saving Toronto...");
        controller.save("Toronto");
        System.out.println("Message: " + viewModel.getMessage());
        System.out.println("Favorites: " + viewModel.getFavorites());

        System.out.println("\nSaving Toronto again (duplicate)...");
        controller.save("Toronto");
        System.out.println("Message: " + viewModel.getMessage());
        System.out.println("Favorites: " + viewModel.getFavorites());

        System.out.println("\nSaving Vancouver...");
        controller.save("Vancouver");
        System.out.println("Message: " + viewModel.getMessage());
        System.out.println("Favorites: " + viewModel.getFavorites());
    }
}
