package interface_adapter.weatherReportPage;

import interface_adapter.ViewModel;

public class WeatherReportPageViewModel extends ViewModel<WeatherReportPageState>{
    public static final String TO_SEARCH_LABEL = "Go Back";
    public static final String TO_HOME_LABEL = "Go to Home";
    public static final String FAVOURITE_LABEL = "Favourite";

    public WeatherReportPageViewModel() {
        super("Weather Report View");
        setState(new WeatherReportPageState());
    }
}
