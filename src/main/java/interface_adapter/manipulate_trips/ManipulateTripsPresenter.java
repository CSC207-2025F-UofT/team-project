package interface_adapter.manipulate_trips;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import use_case.manipulate_trips.ListTripsOutputData;
import use_case.manipulate_trips.ManipulateTripsOutputBoundary;
import use_case.manipulate_trips.MergeTripsOutputData;
import use_case.manipulate_trips.PhotoDetails;
import use_case.manipulate_trips.SplitTripOutputData;
import use_case.manipulate_trips.TripDetailsOutputData;
import use_case.manipulate_trips.TripReference;
import use_case.manipulate_trips.TripSummary;

/** Updates the trip liast and trip details screens, and switches between them. */
public class ManipulateTripsPresenter implements ManipulateTripsOutputBoundary {
    public static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("d", Locale.ENGLISH);
    public static final DateTimeFormatter DAY_MONTH = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH);
    public static final DateTimeFormatter FULL = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
    public static final DateTimeFormatter PHOTO_TIME = DateTimeFormatter.ofPattern("d MMM · HH:mm", Locale.ENGLISH);

    private final TripListViewModel viewModel;
    private final TripDetailsViewModel detailsViewModel;
    private final ViewManagerModel viewManagerModel;

    public ManipulateTripsPresenter(TripListViewModel viewModel, TripDetailsViewModel detailsViewModel,
                                    ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.detailsViewModel = detailsViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override 
    public void prepareTripListView(ListTripsOutputData outputData) {
        List<TripListItem> items = outputData.getTrips().stream()
                .map(ManipulateTripsPresenter::toItem)
                .collect(Collectors.toList());

        TripListState state = viewModel.getState();
        state.setItems(items);
        state.setErrorMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override 
    public void prepareTripListFailView(String errorMessage) {
        TripListState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    /** Fills details screen with a fresh state and switches to it. */
    @Override
    public void prepareTripDetailsView(TripDetailsOutputData outputData) {
        TripDetailsState state = new TripDetailsState();
        fillDetails(state, outputData);
        showDetails(state);
    }

    /** Still opens details screen, showing only the error and back button. */
    @Override 
    public void prepareTripDetailsFailView(String errorMessage) {
        TripDetailsState state = new TripDetailsState();
        state.setErrorMessage(errorMessage);
        showDetails(state);
    }

    /** Stays on the original trip, which now only has photos before the split. */
    @Override
    public void prepareSplitSuccessView(SplitTripOutputData outputData) {
        TripDetailsState state = detailsViewModel.getState();
        fillDetails(state, outputData.getOriginalTrip());
        state.setErrorMessage(null);
        state.setNoticeMessage("Split off “" + outputData.getNewTripName() + "” as a new trip.");
        detailsViewModel.setState(state);
        detailsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareSplitFailView(String errorMessage) {
        showDetailsError(errorMessage);
    }

    /** Opens the merged trip, wherever the merge started. */
    @Override 
    public void prepareMergeSuccessView(MergeTripsOutputData outputData) {
        TripDetailsState state = new TripDetailsState();
        fillDetails(state, outputData.getMergedTrip());
        state.setNoticeMessage("Merged " + outputData.getMergedCount() + " trips into “"
                + outputData.getMergedTrip().getName() + "”.");
        showDetails(state);
    }


    /** Shows error on whichever screen started the merge. */
    @Override 
    public void prepareMergeFailView(String errorMessage) {
        if (ViewNames.TRIPS.equals(viewManagerModel.getActiveView())) {
            prepareTripListFailView(errorMessage);
        } else {
            showDetailsError(errorMessage);
        }
    }

    private void showDetails(TripDetailsState state) {
        detailsViewModel.setState(state);
        detailsViewModel.firePropertyChanged();
        viewManagerModel.setActiveView(ViewNames.TRIP_DETAILS);
    }

    /** Keeps showing current trip, but adds the error. */
    private void showDetailsError(String errorMessage) {
        TripDetailsState state = detailsViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setNoticeMessage(null);
        detailsViewModel.setState(state);
        detailsViewModel.firePropertyChanged();
    }

    private static void fillDetails(TripDetailsState state, TripDetailsOutputData trip) {
        state.setTripId(trip.getTripId());
        state.setName(trip.getName());
        state.setDateRange(formatDateRange(trip.getStartTime(), trip.getEndTime()));
        state.setPhotoCount(formatPhotoCount(trip.getPhotos().size()));
        state.setPhotos(trip.getPhotos().stream()
                .map(ManipulateTripsPresenter::toTile)
                .collect(Collectors.toList()));
        state.setMergeOptions(trip.getOtherTrips().stream()
                .map(ManipulateTripsPresenter::toMergeOption)
                .collect(Collectors.toList()));
        state.setSuggestedSplitName(trip.getName() + " (2)");
    }

    private static TripListItem toItem(TripSummary trip) {
        return new TripListItem(trip.getId(), trip.getName(),
                formatDateRange(trip.getStartTime(), trip.getEndTime()),
                formatDetails(trip.getLocationLabel(), trip.getPhotoCount()),
                trip.getCoverPhotoId());
    }

    private static PhotoTile toTile(PhotoDetails photo) {
        return new PhotoTile(photo.getPhotoId(), formatPhotoTime(photo.getTime()));
    }

    private static MergeOption toMergeOption(TripReference trip) {
        String dates = formatDateRange(trip.getStartTime(), trip.getEndTime());
        return new MergeOption(trip.getTripId(), trip.getName() + " (" + dates + ")");
    }

    /** "13 Nov 2026", "13-14 Nov 2026", "13 Nov - 2 Dec 2026" */
    static String formatDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            return "Unknown dates";
        }
        if (end == null || start.toLocalDate().equals(end.toLocalDate())) {
            return FULL.format(start);
        }
        if (start.getYear() != end.getYear()) {
            return FULL.format(start) + " – " + FULL.format(end);
        }
        if (start.getMonth() != end.getMonth()) {
            return DAY_MONTH.format(start) + " – " + FULL.format(end);
        }
        return DAY.format(start) + "–" + FULL.format(end);
    }

    /** "Montreal, Canada · 5 photos". */
    static String formatDetails(String locationLabel, int photoCount) {
        String location = locationLabel == null ? "Unknown location" : locationLabel;
        return location + " · " + formatPhotoCount(photoCount);
    }

    /** "1 photo", "5 photos". */
    static String formatPhotoCount(int count) {
        return count == 1 ? "1 photo" : count + " photos";
    }

    /** "16 May · 10:00", or "No date". */
    static String formatPhotoTime(LocalDateTime time) {
        return time == null ? "No date" : PHOTO_TIME.format(time);
    }
}