import data_access.FavoriteLocationsGateway;
import data_access.OutfitsGateway;
import entities.Outfit;

import interface_adapters.EditFavoriteLocationController;
import interface_adapters.EditFavoriteLocationPresenter;
import interface_adapters.SavedItemsController;
import interface_adapters.SavedItemsPresenter;
import interface_adapters.SavedItemsViewModel;
import interface_adapters.SaveOutfitController;
import interface_adapters.SaveOutfitPresenter;
import interface_adapters.SaveOutfitViewModel;

import use_case.save.SaveOutfitInteractor;
import use_case.edit.EditFavoriteLocationInteractor;
import use_case.view.ViewSavedItemsInteractor;

import view.SavedItemsView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple Main to test the SavedItemsView with in-memory gateways.
 */
public class Main {

    public static void main(String[] args) {
        // --- In-memory FavoriteLocationsGateway implementation for testing ---
        FavoriteLocationsGateway favGateway = new FavoriteLocationsGateway() {

            private final List<String> favorites = new ArrayList<>(
                    List.of("Melbourne", "Toronto", "Tokyo")
            );

            @Override
            public boolean existsByName(String cityName) {
                return favorites.stream()
                        .anyMatch(c -> c.equalsIgnoreCase(cityName));
            }

            @Override
            public void save(String cityName) {
                if (!existsByName(cityName)) {
                    favorites.add(cityName);
                }
            }

            @Override
            public void delete(String cityName) {
                favorites.removeIf(c -> c.equalsIgnoreCase(cityName));
            }

            @Override
            public boolean saveFavorites(String cityName) {
                // mimic a boolean-returning save
                if (existsByName(cityName)) {
                    return true;
                }
                favorites.add(cityName);
                return true;
            }

            @Override
            public List<String> getFavorites() {
                return new ArrayList<>(favorites);
            }
        };

        // --- In-memory OutfitsGateway implementation for testing ---
        OutfitsGateway outfitsGateway = new OutfitsGateway() {

            private final List<Outfit> outfits = new ArrayList<>(
                    List.of(
                            new Outfit(
                                    "Rainy Day",
                                    Arrays.asList("Rain jacket", "Boots"),
                                    "Rainy, 10–15°C",
                                    "Melbourne"
                            ),
                            new Outfit(
                                    "Cold Toronto",
                                    Arrays.asList("Puffer jacket", "Scarf", "Gloves"),
                                    "Snowy, -5–0°C",
                                    "Toronto"
                            )
                    )
            );

            @Override
            public boolean exists(String name, String weatherProfile, String location) {
                return outfits.stream().anyMatch(o ->
                        o.getName().equalsIgnoreCase(name)
                                && o.getWeatherProfile().equalsIgnoreCase(weatherProfile)
                                && o.getLocation().equalsIgnoreCase(location)
                );
            }

            @Override
            public void save(Outfit outfit) {
                // simple overwrite by same name + location + profile
                outfits.removeIf(o ->
                        o.getName().equalsIgnoreCase(outfit.getName())
                                && o.getWeatherProfile().equalsIgnoreCase(outfit.getWeatherProfile())
                                && o.getLocation().equalsIgnoreCase(outfit.getLocation())
                );
                outfits.add(outfit);
            }

            @Override
            public List<Outfit> getAll() {
                return new ArrayList<>(outfits);
            }
        };

        // --- ViewModels ---
        SavedItemsViewModel savedItemsVm = new SavedItemsViewModel();
        SaveOutfitViewModel saveOutfitVm = new SaveOutfitViewModel();

        // --- UC6: ViewSavedItems stack ---
        SavedItemsPresenter savedItemsPresenter =
                new SavedItemsPresenter(savedItemsVm);
        ViewSavedItemsInteractor viewSavedItemsInteractor =
                new ViewSavedItemsInteractor(outfitsGateway, favGateway, savedItemsPresenter);
        SavedItemsController savedItemsController =
                new SavedItemsController(viewSavedItemsInteractor);

        // --- UC6: EditFavoriteLocation stack ---
        EditFavoriteLocationPresenter editFavPresenter =
                new EditFavoriteLocationPresenter(savedItemsVm);
        EditFavoriteLocationInteractor editFavInteractor =
                new EditFavoriteLocationInteractor(favGateway, editFavPresenter);
        EditFavoriteLocationController editFavController =
                new EditFavoriteLocationController(editFavInteractor);

        // --- UC5: SaveOutfit stack (reused for editing outfits) ---
        SaveOutfitPresenter saveOutfitPresenter =
                new SaveOutfitPresenter(saveOutfitVm);
        SaveOutfitInteractor saveOutfitInteractor =
                new SaveOutfitInteractor(outfitsGateway, saveOutfitPresenter);
        SaveOutfitController saveOutfitController =
                new SaveOutfitController(saveOutfitInteractor);

        // --- Swing UI wiring ---
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Weather2Wear – Saved Items");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            SavedItemsView savedItemsView = new SavedItemsView(
                    savedItemsController,
                    editFavController,
                    saveOutfitController,
                    savedItemsVm,
                    saveOutfitVm
            );

            frame.setContentPane(savedItemsView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
