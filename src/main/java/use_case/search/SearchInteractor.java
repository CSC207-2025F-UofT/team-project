package use_case.search;

import entity.Location;

/**
 * The Login Interactor.
 */
public class SearchInteractor implements SearchInputBoundary {
    private final SearchDataAccessInterface searchDataAccessObj;
    private final SearchOutputBoundary searchPresenter;

    public SearchInteractor(SearchDataAccessInterface userDataAccessInterface,
                            SearchOutputBoundary searchOutputBoundary) {
        this.searchDataAccessObj = userDataAccessInterface;
        this.searchPresenter = searchOutputBoundary;
    }

    @Override
    public void execute(SearchInputData searchInputData) throws Exception {
        final String locationName = searchInputData.getLocationName();
        if (!searchDataAccessObj.existsByName(locationName)) {
            searchPresenter.prepareFailView(locationName + ": Location does not exist.");
        }
        else {
//            get location data from OSM
            final Location location = searchDataAccessObj.get(searchInputData.getLocationName());

            final SearchOutputData searchOutputData = new SearchOutputData(location.getName(),
                    location.getLatitude(), location.getLongitude());
            searchPresenter.prepareSuccessView(searchOutputData);
        }
    }
}
