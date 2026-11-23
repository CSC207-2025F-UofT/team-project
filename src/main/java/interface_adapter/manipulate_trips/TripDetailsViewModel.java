package interface_adapter.manipulate_trips;

import interface_adapter.ViewModel;
import interface_adapter.ViewNames;

public class TripDetailsViewModel extends ViewModel<TripDetailsState> {
    public TripDetailsViewModel() {
        super(ViewNames.TRIP_DETAILS);
        setState(new TripDetailsState());
    }
}