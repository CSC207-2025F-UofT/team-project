package use_case.logout;

import data_access.InMemoryUserDataAccessObject;
import entity.Student;
import entity.StudentFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutInteractorTest {

    @Test
    void successTest() {
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Paul to the data access repository before we log in.
        StudentFactory factory = new StudentFactory();
        Student student = factory.create("Paul", "password");
        userRepository.save(student);
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