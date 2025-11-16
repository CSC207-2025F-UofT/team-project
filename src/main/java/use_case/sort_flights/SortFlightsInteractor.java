package use_case.sort_flights;

import entity.Flight;
import java.util.Comparator;
import java.util.List;

public class SortFlightsInteractor implements SortFlightsInputBoundary {
    private final SortFlightsOutputBoundary sortFlightsPresenter;

    public SortFlightsInteractor(SortFlightsOutputBoundary sortFlightsPresenter) {
        this.sortFlightsPresenter = sortFlightsPresenter;
    }

    @Override
    public void execute(SortFlightsInputData sortFlightsInputData) {
        List<Flight> flights = sortFlightsInputData.getFlights();
        String sortType = sortFlightsInputData.getSortType();

        if (sortType.equals("PRICE")) {
            // Sort by price (lowest first)
            // THE FIX: Use a lambda to access the public 'priceTotal' field
            flights.sort(Comparator.comparingDouble(flight -> flight.priceTotal));

        } else {
            // Default to sorting by duration (shortest first)
            flights.sort(Comparator.comparing(flight -> {

                String duration = flight.duration;
                int hours = 0;
                int minutes = 0;
                int hIndex = duration.indexOf("h");
                int mIndex = duration.indexOf("m");

                if (hIndex != -1) {
                    hours = Integer.parseInt(duration.substring(0, hIndex));
                }
                if (mIndex != -1) {
                    String minStr = duration.substring(hIndex + 1, mIndex);
                    minutes = Integer.parseInt(minStr);
                }
                return (hours * 60) + minutes;
            }));
        }

        SortFlightsOutputData outputData = new SortFlightsOutputData(flights);
        sortFlightsPresenter.prepareSuccessView(outputData);
    }
}
