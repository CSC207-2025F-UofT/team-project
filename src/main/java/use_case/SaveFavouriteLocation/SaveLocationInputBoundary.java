package use_case.SaveFavouriteLocation;

import api.geocodingapi.CoordinatesFetcher;
import use_case.DeleteFavouriteLocation.DeleteLocationInputData;

/**
 * Input Boundary for actions which are related to saving a favourite location.
 */
public interface SaveLocationInputBoundary {
    void execute(SaveLocationInputData deleteInputData) throws CoordinatesFetcher.CityNotFoundException;
}
