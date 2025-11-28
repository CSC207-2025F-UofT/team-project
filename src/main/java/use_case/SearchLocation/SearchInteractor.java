package use_case.SearchLocation;

import entity.location.Location;
import entity.location.LocationFactory;
import entity.weatherReport.WeatherReport;

/**
 * The Search Interactor
 */
public class SearchInteractor implements SearchInputBoundary {
    private final SearchUserDataAccessInterface userDataAccessObject;
    private final SearchOutputBoundary searchPresenter;

    public SearchInteractor(SearchUserDataAccessInterface userDataAccessInterface,
                            SearchOutputBoundary searchOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.searchPresenter = searchOutputBoundary;
    }
    @Override
    public void execute(SearchInputData searchInputData){
        final String cityName = searchInputData.getCityName();
        if (cityName == null || cityName.isEmpty()){
            searchPresenter.prepareFailView("please enter a city name");
        }
        // elif city does not exist
        else {
            final Location location = userDataAccessObject.get(searchInputData.getCityName());
            userDataAccessObject.setCurrentCityName(cityName);
            final SearchOutputData searchOutputData = new SearchOutputData(location.getName());
            searchPresenter.prepareSuccessView(searchOutputData);
        }
    }

}
