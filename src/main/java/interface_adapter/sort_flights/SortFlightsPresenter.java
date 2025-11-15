package interface_adapter.sort_flights;

import interface_adapter.flight_results.FlightResultsState;
import interface_adapter.flight_results.FlightResultsViewModel;
import use_case.sort_flights.SortFlightsOutputBoundary;
import use_case.sort_flights.SortFlightsOutputData;

public class SortFlightsPresenter implements SortFlightsOutputBoundary {
    private final FlightResultsViewModel flightResultsViewModel;

    public SortFlightsPresenter(FlightResultsViewModel flightResultsViewModel) {
        this.flightResultsViewModel = flightResultsViewModel;
    }

    @Override
    public void prepareSuccessView(SortFlightsOutputData sortFlightsOutputData) {
        FlightResultsState currentState = flightResultsViewModel.getState();
        currentState.setFlights(sortFlightsOutputData.getSortedFlights());

        // This fires the "state" property change,
        // which our FlightResultsView is listening to.
        flightResultsViewModel.firePropertyChange();
    }
}
