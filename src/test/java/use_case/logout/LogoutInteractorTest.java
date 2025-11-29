package use_case.logout;

import data_access.InMemoryUserDataAccessObject;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.user_logout.UserLogoutInteractor;
import use_case.user_logout.UserLogoutOutputBoundary;
import use_case.user_logout.UserLogoutOutputData;

import static org.junit.jupiter.api.Assertions.*;

class LogoutInteractorTest {

    static class FakeLogoutPresenter implements UserLogoutOutputBoundary {
        UserLogoutOutputData receivedData = null;

        @Override
        public void prepareSuccessView(UserLogoutOutputData outputData) {
            this.receivedData = outputData;
        }
    }

    @Test
    void testLogoutSuccess() {
        // Arrange
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        FakeLogoutPresenter presenter = new FakeLogoutPresenter();

        UserLogoutInteractor interactor = new UserLogoutInteractor(dao, presenter);

        dao.setCurrentUsername("Alice");
        interactor.execute();
        // presenter get output
        assertNotNull(presenter.receivedData);
        assertEquals("Alice", presenter.receivedData.getUsername());

        // clear currentUsername
        assertNull(dao.getCurrentUsername());
    }
}
