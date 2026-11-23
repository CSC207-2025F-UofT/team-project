package interface_adapter.manipulate_trips;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import use_case.manipulate_trips.ListTripsOutputData;
import use_case.manipulate_trips.MergeTripsOutputData;
import use_case.manipulate_trips.SplitTripOutputData;
import use_case.manipulate_trips.TripDetailsOutputData;
import use_case.manipulate_trips.PhotoDetails;
import use_case.manipulate_trips.TripReference;
import use_case.manipulate_trips.TripSummary;

import static org.junit.jupiter.api.Assertions.*;

class ManipulateTripsPresenterTest {

    private final TripDetailsViewModel detailsViewModel = new TripDetailsViewModel();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    private static LocalDateTime at(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 12, 0);
    }

    private ManipulateTripsPresenter presenterFor(TripListViewModel listViewModel) {
        return new ManipulateTripsPresenter(listViewModel, detailsViewModel, viewManagerModel);
    }



    @Test
    void dateRangeFormats() {
        assertEquals("16 May 2025", ManipulateTripsPresenter.formatDateRange(at(2025, 5, 16), at(2025, 5, 16)));
        assertEquals("16–17 May 2025", ManipulateTripsPresenter.formatDateRange(at(2025, 5, 16), at(2025, 5, 17)));
        assertEquals("30 May – 2 Jun 2025", ManipulateTripsPresenter.formatDateRange(at(2025, 5, 30), at(2025, 6, 2)));
        assertEquals("30 Dec 2024 – 2 Jan 2025",
                ManipulateTripsPresenter.formatDateRange(at(2024, 12, 30), at(2025, 1, 2)));
        assertEquals("Unknown dates", ManipulateTripsPresenter.formatDateRange(null, null));
    }

    @Test
    void detailsFormats() {
        assertEquals("Montreal, Canada · 5 photos", ManipulateTripsPresenter.formatDetails("Montreal, Canada", 5));
        assertEquals("Unknown location · 1 photo", ManipulateTripsPresenter.formatDetails(null, 1));
    }

    @Test
    void successFillsStateClearsErrorAndFires() {
        TripListViewModel viewModel = new TripListViewModel();
        viewModel.getState().setErrorMessage("old error");
        int[] fires = {0};
        viewModel.addPropertyChangeListener(evt -> fires[0]++);

        TripSummary summary = new TripSummary("t1", "Montreal Weekend", at(2025, 5, 16), at(2025, 5, 17),
                5, "Montreal, Canada", "p1");
        presenterFor(viewModel).prepareTripListView(new ListTripsOutputData(List.of(summary)));

        TripListItem item = viewModel.getState().getItems().get(0);
        assertEquals("t1", item.getTripId());
        assertEquals("Montreal Weekend", item.getName());
        assertEquals("16–17 May 2025", item.getDateRange());
        assertEquals("Montreal, Canada · 5 photos", item.getDetails());
        assertEquals("p1", item.getCoverPhotoId());
        assertNull(viewModel.getState().getErrorMessage());
        assertEquals(1, fires[0]);
    }

    @Test
    void tripWithoutCoverPassesNullThrough() {
        TripListViewModel viewModel = new TripListViewModel();
        TripSummary summary = new TripSummary("t1", "Empty", null, null, 0, null, null);

        presenterFor(viewModel).prepareTripListView(new ListTripsOutputData(List.of(summary)));

        assertNull(viewModel.getState().getItems().get(0).getCoverPhotoId());
    }

    @Test
    void failSetsErrorAndFires() {
        TripListViewModel viewModel = new TripListViewModel();
        int[] fires = {0};
        viewModel.addPropertyChangeListener(evt -> fires[0]++);

        presenterFor(viewModel).prepareTripListFailView("boom");

        assertEquals("boom", viewModel.getState().getErrorMessage());
        assertEquals(1, fires[0]);
    }


    private static LocalDateTime time(int month, int day, int hour) {
        return LocalDateTime.of(2025, month, day, hour, 0);
    }

    private static TripDetailsOutputData montreal() {
        return new TripDetailsOutputData("t1", "Montreal Weekend", time(5,16,10), time(5,17,9),
                List.of(new PhotoDetails("p1", time(5,16,10)), new PhotoDetails("p2", null)),
                List.of(new TripReference("t2", "Tokyo", time(11,12,8), time(11,17,20))));
    }

    @Test
    void photoFormats() {
        assertEquals("16 May · 10:00", ManipulateTripsPresenter.formatPhotoTime(time(5, 16, 10)));
        assertEquals("No date", ManipulateTripsPresenter.formatPhotoTime(null));
        assertEquals("1 photo", ManipulateTripsPresenter.formatPhotoCount(1));
    }

    @Test
    void detailsFillStateAndOpenDetailsScreen() {
        int[] fires = {0};
        detailsViewModel.addPropertyChangeListener(evt -> fires[0]++);

        presenterFor(new TripListViewModel()).prepareTripDetailsView(montreal());

        TripDetailsState state = detailsViewModel.getState();
        assertEquals("t1", state.getTripId());
        assertEquals("Montreal Weekend", state.getName());
        assertEquals("16–17 May 2025", state.getDateRange());
        assertEquals("2 photos", state.getPhotoCount());
        assertEquals("16 May · 10:00", state.getPhotos().get(0).getCaption());
        assertEquals("No date", state.getPhotos().get(1).getCaption());
        assertEquals("t2", state.getMergeOptions().get(0).getTripId());
        assertEquals("Tokyo (12–17 Nov 2025)", state.getMergeOptions().get(0).getLabel());
        assertEquals("Montreal Weekend (2)", state.getSuggestedSplitName());
        assertNull(state.getErrorMessage());
        assertEquals(1, fires[0]);
        assertEquals(ViewNames.TRIP_DETAILS, viewManagerModel.getActiveView());
    }

    @Test
    void openingAnotherTripClearsOldMessages() {
        detailsViewModel.getState().setNoticeMessage("Merged 2 trips.");
        detailsViewModel.getState().setErrorMessage("old error");

        presenterFor(new TripListViewModel()).prepareTripDetailsView(montreal());

        assertNull(detailsViewModel.getState().getNoticeMessage());
        assertNull(detailsViewModel.getState().getErrorMessage());
    }

    @Test
    void detailsFailShowsOnlyTheErrorAndStillOpensDetailsScreen() {
        ManipulateTripsPresenter presenter = presenterFor(new TripListViewModel());
        presenter.prepareTripDetailsView(montreal());

        presenter.prepareTripDetailsFailView("Couldn't open trip: gone");

        assertEquals("Couldn't open trip: gone", detailsViewModel.getState().getErrorMessage());
        assertNull(detailsViewModel.getState().getTripId());
        assertTrue(detailsViewModel.getState().getPhotos().isEmpty());
        assertEquals(ViewNames.TRIP_DETAILS, viewManagerModel.getActiveView());
    }

    @Test
    void splitSuccessShowsShorterTripAndNotice() {
        detailsViewModel.getState().setErrorMessage("old error");
        LocalDateTime day = time(5, 16, 10);
        TripDetailsOutputData remaining = new TripDetailsOutputData("t1", "Trip", day, day,
                List.of(new PhotoDetails("p1", day)), List.of());

        presenterFor(new TripListViewModel())
                .prepareSplitSuccessView(new SplitTripOutputData(remaining, "t2", "Second half"));

        assertEquals("1 photo", detailsViewModel.getState().getPhotoCount());
        assertEquals("Split off “Second half” as a new trip.", detailsViewModel.getState().getNoticeMessage());
        assertNull(detailsViewModel.getState().getErrorMessage());
    }

    @Test
    void splitFailKeepsTripAndShowsError() {
        detailsViewModel.getState().setTripId("t1");
        detailsViewModel.getState().setNoticeMessage("old notice");

        presenterFor(new TripListViewModel()).prepareSplitFailView("Enter a name for the new trip.");

        assertEquals("t1", detailsViewModel.getState().getTripId());
        assertEquals("Enter a name for the new trip.", detailsViewModel.getState().getErrorMessage());
        assertNull(detailsViewModel.getState().getNoticeMessage());
    }

    @Test
    void mergeSuccessOpensMergedTripWithNotice() {
        viewManagerModel.setActiveView(ViewNames.TRIPS);
        LocalDateTime day = time(5, 16, 10);
        TripDetailsOutputData merged = new TripDetailsOutputData("t1", "Montreal", day, day, List.of(), List.of());

        presenterFor(new TripListViewModel()).prepareMergeSuccessView(new MergeTripsOutputData(merged, 3));

        assertEquals(ViewNames.TRIP_DETAILS, viewManagerModel.getActiveView());
        assertEquals("t1", detailsViewModel.getState().getTripId());
        assertEquals("Merged 3 trips into “Montreal”.", detailsViewModel.getState().getNoticeMessage());
    }

    @Test
    void mergeFailFromTripListShowsOnTripList() {
        TripListViewModel listViewModel = new TripListViewModel();
        viewManagerModel.setActiveView(ViewNames.TRIPS);

        presenterFor(listViewModel).prepareMergeFailView("Select at least two trips to merge.");

        assertEquals("Select at least two trips to merge.", listViewModel.getState().getErrorMessage());
        assertNull(detailsViewModel.getState().getErrorMessage());
        assertEquals(ViewNames.TRIPS, viewManagerModel.getActiveView());
    }

    @Test
    void mergeFailFromDetailsShowsOnDetails() {
        TripListViewModel listViewModel = new TripListViewModel();
        viewManagerModel.setActiveView(ViewNames.TRIP_DETAILS);

        presenterFor(listViewModel).prepareMergeFailView("Couldn't merge trips: boom");

        assertEquals("Couldn't merge trips: boom", detailsViewModel.getState().getErrorMessage());
        assertNull(listViewModel.getState().getErrorMessage());
    }
}