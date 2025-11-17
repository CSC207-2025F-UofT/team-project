package use_case;

import entities.Location;
import entities.WeatherData;

public class GetWeatherInteractor implements GetWeatherInputBoundary {

    private final WeatherDataGateway weatherGateway;
    private final GetWeatherOutputBoundary presenter;

    public GetWeatherInteractor(WeatherDataGateway weatherGateway,
                                GetWeatherOutputBoundary presenter) {
        this.weatherGateway = weatherGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetWeatherInputData inputData) {

        // Build the Location entity from input
        Location location = new Location(inputData.getName(), inputData.getCountryCode(), inputData.getLatitude(), inputData.getLongitude());

        // Fetch actual weather data
        WeatherData data = weatherGateway.fetch(location);

        // Convert WeatherData → GetWeatherOutputData
        GetWeatherOutputData output = new GetWeatherOutputData(
                data.getTemperature(),
                data.isRaining(),          // public field in your class
                data.getWindSpeed()
        );

        presenter.present(output);
    }
}
