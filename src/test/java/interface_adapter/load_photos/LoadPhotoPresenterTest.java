package interface_adapter.load_photos;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import use_case.load_photos.LoadPhotoOutputData;

import static org.junit.jupiter.api.Assertions.*;

class LoadPhotoPresenterTest {
    private PhotoImageViewModel viewModel;
    private LoadPhotoPresenter presenter;
    private List<PhotoImageState> events;

    @BeforeEach
    void setUp() {
        viewModel = new PhotoImageViewModel();
        presenter = new LoadPhotoPresenter(viewModel);
        events = new ArrayList<>();
        viewModel.addPropertyChangeListener(evt -> events.add((PhotoImageState) evt.getNewValue()));
    }

    @Test
    void successPublishesBytesOnce() {
        byte[] bytes = {1, 2, 3};
        presenter.prepareSuccessView(new LoadPhotoOutputData("p1", bytes));

        assertEquals(1, events.size());
        assertEquals("p1", events.get(0).getPhotoId());
        assertArrayEquals(bytes, events.get(0).getImageBytes());
        assertSame(events.get(0), viewModel.getState());
    }

    @Test
    void failurePublishesNullBytes() {
        presenter.prepareFailView("p1", "gone");

        assertEquals(1, events.size());
        assertEquals("p1", events.get(0).getPhotoId());
        assertNull(events.get(0).getImageBytes());
    }

    @Test
    void eachResultIsItsOwnStateObject() {
        presenter.prepareSuccessView(new LoadPhotoOutputData("p1", new byte[] {1}));
        presenter.prepareSuccessView(new LoadPhotoOutputData("p2", new byte[] {2}));

        assertNotSame(events.get(0), events.get(1));
        assertEquals("p1", events.get(0).getPhotoId());   // first event unchanged by the second
    }
}