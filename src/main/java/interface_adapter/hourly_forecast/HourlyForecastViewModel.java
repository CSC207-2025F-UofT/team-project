package interface_adapter.hourly_forecast;

import interface_adapter.ViewModel;

public class HourlyForecastViewModel extends ViewModel<HourlyForecastState> {
    public static final String TITLE_LABEL = "Hourly Forecast";
    public static final String GO_HOME_LABEL = "Go Home";
    public static final String GO_BACK_LABEL = "Go Back";

    public HourlyForecastViewModel() {
        super("Hourly Forecast View");
        setState(new HourlyForecastState());
    }
}
