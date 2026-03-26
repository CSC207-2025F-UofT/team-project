package use_case.deleteFavouriteLocation;

import data_access.InMemoryUserDataAccessObject;
import entity.user.User;
import entity.user.UserFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteLocationInteractorTest {

    @Test
    void successfulDeletionTest() {
        // Create a test user with 2 saved locations
        UserFactory factory = new UserFactory();
        User user = factory.create("username", "password");
        user.addLocation("Toronto");
        user.addLocation("London");

        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();
        userRepository.save(user);

        // Set the dummy user as the current user
        userRepository.setCurrentUsername(user.getName());

        DeleteLocationInputBoundary interactor = getDeleteLocationInputBoundary(userRepository);

        // We remove one location
        DeleteLocationInputData inputData = new DeleteLocationInputData("Toronto");
        interactor.execute(inputData);

        // We check that only 1 location was removed ("Toronto")
        // We also check that only 1 location remains ("London")
        User updatedUser = userRepository.get(user.getName());
        List<String> remaining = updatedUser.getLocations();
        assertEquals(1, remaining.size());
        assertFalse(remaining.contains("Toronto"));
        assertTrue(remaining.contains("London"));
    }

    @NotNull
    private static DeleteLocationInputBoundary getDeleteLocationInputBoundary(DeleteLocationUserDataAccessInterface userRepository) {
        DeleteLocationOutputBoundary presenter = new DeleteLocationOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteLocationOutputData outputData) {
                // nothing should happen...
            }

            @Override
            public void prepareFailView(String error) {
                fail("Deletion should succeed, failure is unexpected");
            }
        };

        return new DeleteLocationInteractor(presenter, userRepository);
    }
}