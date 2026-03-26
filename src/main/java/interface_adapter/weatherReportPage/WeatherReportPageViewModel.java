package interface_adapter.weatherReportPage;

import interface_adapter.ViewModel;

public class WeatherReportPageViewModel extends ViewModel<WeatherReportPageState>{
    public static final String TO_SEARCH_LABEL = "Search";
    public static final String TO_HOME_LABEL = "Go to Home";
    public static final String FAVOURITE_LABEL = "Add to Favourite";
    public static final String CHECK_OUTFIT_LABEL = "Check outfit";
    public static final String UNFAVOURITE_LABEL = "Remove from Favourites";
    public static final String GO_FAVOURITES = "My favourites";
    public static final String TO_HOURLY_FORECAST = "Hourly Forecast";

    public WeatherReportPageViewModel() {
        super("Weather Report View");
        setState(new WeatherReportPageState());
    }
}
