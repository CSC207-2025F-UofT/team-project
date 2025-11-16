package interface_adapters.save_favorite_loc;

import use_case.save_favorite.SaveFavoriteLocationInputBoundary;
import use_case.save_favorite.SaveFavoriteLocationInputData;

public class FavoriteLocationsController {
    private final SaveFavoriteLocationInputBoundary interactor;

    public FavoriteLocationsController(SaveFavoriteLocationInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void save(String cityName){
        SaveFavoriteLocationInputData inputData = new SaveFavoriteLocationInputData(cityName);
        interactor.execute(inputData);
    }
}