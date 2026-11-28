package use_case.logout;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LogoutInteractorTest {

    @Test
    void logsOutTheCurrentUser() {
        final FakeUsers users = new FakeUsers("u1");
        final RecordingPresenter presenter = new RecordingPresenter();

        new LogoutInteractor(users, presenter).execute();

        assertNull(users.getCurrentUserId());
        assertTrue(presenter.output.wasLoggedIn());
    }

    @Test
    void showsTheGalleryWhenNoUserIsLoggedIn() {
        final FakeUsers users = new FakeUsers(null);
        final RecordingPresenter presenter = new RecordingPresenter();

        new LogoutInteractor(users, presenter).execute();

        assertNull(users.getCurrentUserId());
        assertFalse(presenter.output.wasLoggedIn());
    }

    @Test
    void treatsABlankIdAsNotLoggedIn() {
        final RecordingPresenter presenter = new RecordingPresenter();

        new LogoutInteractor(new FakeUsers("  "), presenter).execute();

        assertFalse(presenter.output.wasLoggedIn());
    }

    private static final class FakeUsers implements LogoutUserDataAccessInterface {
        private String currentUserId;

        FakeUsers(String currentUserId) {
            this.currentUserId = currentUserId;
        }

        @Override
        public String getCurrentUserId() {
            return currentUserId;
        }

        @Override
        public void setCurrentUserId(String id) {
            currentUserId = id;
        }
    }

    private static final class RecordingPresenter implements LogoutOutputBoundary {
        private LogoutOutputData output;

        @Override
        public void prepareSuccessView(LogoutOutputData outputData) {
            output = outputData;
        }
    }
}
