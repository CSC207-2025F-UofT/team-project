package use_case.SaveFavouriteLocation;

import api.geocodingapi.CoordinatesFetcher;

/**
 * Input Boundary for actions which are related to saving a favourite location.
 */
public interface SaveLocationInputBoundary {
    void execute(SaveLocationInputData saveInputData) throws CoordinatesFetcher.CityNotFoundException;
}
