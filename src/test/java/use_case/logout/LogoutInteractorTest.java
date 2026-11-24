package use_case.logout;

import data_access.calogin.InMemoryUserDataAccessObject;
import entity.calogin.UserFactory;
import entity.calogin.User;
import org.junit.jupiter.api.Test;
import use_case.calogin.logout.LogoutInputBoundary;
import use_case.calogin.logout.LogoutInteractor;
import use_case.calogin.logout.LogoutOutputBoundary;
import use_case.calogin.logout.LogoutOutputData;

import static org.junit.jupiter.api.Assertions.*;

class LogoutInteractorTest {

    @Test
    void successTest() {
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Paul to the data access repository before we log in.
        UserFactory factory = new UserFactory();
        User user = factory.create("Paul", "password");
        userRepository.save(user);
        userRepository.setCurrentUsername("Paul");

        // This creates a successPresenter that tests whether the test case is as we expect.
        LogoutOutputBoundary successPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData user) {
                assertEquals("Paul", user.getUsername());
                assertNull(userRepository.getCurrentUsername());
            }
};

        LogoutInputBoundary interactor = new LogoutInteractor(userRepository, successPresenter);
        interactor.execute();
        assertNull(userRepository.getCurrentUsername());
    }

}