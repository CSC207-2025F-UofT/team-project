package interface_adapter.sort_flights;

import entity.Flight;
import use_case.sort_flights.SortFlightsInputBoundary;
import use_case.sort_flights.SortFlightsInputData;
import java.util.List;

public class SortFlightsController {
    private final SortFlightsInputBoundary sortFlightsInteractor;

    public SortFlightsController(SortFlightsInputBoundary sortFlightsInteractor) {
        this.sortFlightsInteractor = sortFlightsInteractor;
    }

    public void execute(List<Flight> flights, String sortType) {
        SortFlightsInputData inputData = new SortFlightsInputData(flights, sortType);
        sortFlightsInteractor.execute(inputData);
    }
}
