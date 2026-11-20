package use_case.load_photos;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.InMemoryTripDataAcessObject;
import entity.ImageType;
import entity.Photo;
import use_case.ImageReadException;

import static org.junit.jupiter.api.Assertions.*;

class LoadPhotoInteractorTest {
    static class CapturingPresenter implements LoadPhotoOutputBoundary {
        LoadPhotoOutputData success;
        String failedId;
        String failMessage;

        @Override public void prepareSuccessView(LoadPhotoOutputData out) { success = out; }
        @Override public void prepareFailView(String photoId, String message) {
            failedId = photoId;
            failMessage = message;
        }
    }

    private InMemoryTripDataAcessObject photos;
    private CapturingPresenter presenter;
    private Photo stored;

    @BeforeEach
    void setUp() {
        photos = new InMemoryTripDataAcessObject();
        presenter = new CapturingPresenter();
        stored = new Photo("p1", ImageType.JPEG, null, null);
        photos.writePhoto(stored);
    }

    @Test
    void successSendsIdAndBytes() {
        new LoadPhotoInteractor(photos, photo -> new byte[] {1, 2, 3}, presenter)
                .execute(new LoadPhotoInputData("p1"));

        assertNull(presenter.failedId);
        assertEquals("p1", presenter.success.getPhotoId());
        assertArrayEquals(new byte[] {1, 2, 3}, presenter.success.getImageBytes());
    }

    @Test
    void readerReceivesTheStoredPhoto() {
        List<Photo> requested = new ArrayList<>();
        new LoadPhotoInteractor(photos, photo -> { requested.add(photo); return new byte[0]; }, presenter)
                .execute(new LoadPhotoInputData("p1"));

        assertEquals(List.of(stored), requested);
    }

    @Test
    void unknownPhotoFailsWithoutReading() {
        List<Photo> requested = new ArrayList<>();
        new LoadPhotoInteractor(photos, photo -> { requested.add(photo); return new byte[0]; }, presenter)
                .execute(new LoadPhotoInputData("ghost"));

        assertNull(presenter.success);
        assertEquals("ghost", presenter.failedId);
        assertTrue(requested.isEmpty());
    }

    @Test
    void unreadableImageFails() {
        new LoadPhotoInteractor(photos, photo -> { throw new ImageReadException("p1", "file gone"); }, presenter)
                .execute(new LoadPhotoInputData("p1"));

        assertNull(presenter.success);
        assertEquals("p1", presenter.failedId);
        assertTrue(presenter.failMessage.contains("file gone"));
    }
}