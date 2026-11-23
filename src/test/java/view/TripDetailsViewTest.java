package view;

import java.util.List;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import interface_adapter.load_photos.LoadPhotoController;
import interface_adapter.load_photos.PhotoImageViewModel;
import interface_adapter.manipulate_trips.ManipulateTripsController;
import interface_adapter.manipulate_trips.PhotoTile;
import interface_adapter.manipulate_trips.TripDetailsState;
import interface_adapter.manipulate_trips.TripDetailsViewModel;
import view.components.ThumbnailCache;

import static org.junit.jupiter.api.Assertions.*;

class TripDetailsViewTest {
    private TripDetailsViewModel viewModel;
    private ViewManagerModel viewManagerModel;
    private TripDetailsView view;

    @BeforeEach
    void setUp() {
        viewModel = new TripDetailsViewModel();
        viewManagerModel = new ViewManagerModel();
        view = new TripDetailsView(viewModel, viewManagerModel, new ThumbnailCache(64, 10,
                new PhotoImageViewModel(), new LoadPhotoController(input -> { })));
    }

    private void publish(TripDetailsState state) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            viewModel.setState(state);
            viewModel.firePropertyChanged();
        });
    }

    private static TripDetailsState trip(PhotoTile... photos) {
        TripDetailsState state = new TripDetailsState();
        state.setTripId("t1");
        state.setName("Montreal Weekend");
        state.setDateRange("16–17 May 2025");
        state.setPhotoCount(photos.length + " photos");
        state.setPhotos(List.of(photos));
        return state;
    }

    @Test
    void rendersHeaderAndPhotos() throws Exception {
        publish(trip(new PhotoTile("p1", "16 May · 10:00"), new PhotoTile("p2", "16 May · 17:00")));

        assertEquals("Montreal Weekend", view.getNameLabel().getText());
        assertEquals("16–17 May 2025 · 2 photos", view.getSubtitleLabel().getText());
        assertEquals(2, view.getPhotoModel().size());
        assertFalse(view.getMessageLabel().isVisible());
    }

    @Test
    void showsNoticeThenError() throws Exception {
        TripDetailsState state = trip(new PhotoTile("p1", "x"));
        state.setNoticeMessage("Merged 2 trips.");
        publish(state);
        assertEquals("Merged 2 trips.", view.getMessageLabel().getText());

        state.setErrorMessage("boom");
        publish(state);
        assertEquals("boom", view.getMessageLabel().getText());   // errors win over notices
    }

    @Test
    void showsEmptyMessageForTripWithoutPhotos() throws Exception {
        publish(trip());

        assertTrue(view.getMessageLabel().isVisible());
        assertEquals(TripDetailsView.EMPTY_MESSAGE, view.getMessageLabel().getText());
    }

    @Test
    void backButtonReturnsToTripList() {
        view.getBackButton().doClick();

        assertEquals(ViewNames.TRIPS, viewManagerModel.getActiveView());
    }

    @Test
    void splitSendsCurrentTripSelectedPhotoAndName() throws Exception {
        publish(trip(new PhotoTile("p1", "a"), new PhotoTile("p2", "b")));
        RecordingTripsInteractor interactor = new RecordingTripsInteractor();
        view.setManipulateTripsController(new ManipulateTripsController(interactor));

        view.requestSplit("p2", "Second half");

        assertEquals("t1", interactor.split.getTripId());
        assertEquals("p2", interactor.split.getFirstPhotoId());
        assertEquals("Second half", interactor.split.getNewTripName());
    }

    @Test
    void mergeSendsCurrentTripAndChosenTrip() throws Exception {
        publish(trip());
        RecordingTripsInteractor interactor = new RecordingTripsInteractor();
        view.setManipulateTripsController(new ManipulateTripsController(interactor));

        view.requestMerge("t2");

        assertEquals(List.of("t1", "t2"), interactor.merged.getTripIds());
    }
}