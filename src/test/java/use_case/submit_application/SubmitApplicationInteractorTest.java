package use_case.submit_application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.InMemoryApplicationDataAccessObject;
import entity.Application;
import entity.ApplicationStatus;

class SubmitApplicationInteractorTest {

    private InMemoryApplicationDataAccessObject applicationDataAccess;
    private RecordingPresenter presenter;
    private SubmitApplicationInteractor interactor;

    @BeforeEach
    void setUp() {
        applicationDataAccess = new InMemoryApplicationDataAccessObject();
        presenter = new RecordingPresenter();
        interactor = new SubmitApplicationInteractor(applicationDataAccess, new FakePetDataAccess(), presenter);
    }

    private SubmitApplicationInputData input(String petId, String housingType) {
        return new SubmitApplicationInputData("adopter1", petId, housingType, "Had two cats", "2 adults",
                "me@example.com", "I work from home");
    }

    @Test
    void successCreatesPendingApplication() {
        interactor.execute(input("mochi", "Apartment"));

        assertNull(presenter.error);
        assertEquals("Mochi", presenter.success.getPetName());
        final List<Application> saved = applicationDataAccess.getApplicationsByAdopter("adopter1");
        assertEquals(1, saved.size());
        assertEquals("mochi", saved.get(0).getPetId());
        assertEquals(ApplicationStatus.PENDING, saved.get(0).getStatus());
        assertEquals(presenter.success.getApplicationId(), saved.get(0).getId());
    }

    @Test
    void missingRequiredInformationFails() {
        interactor.execute(input("mochi", ""));

        assertEquals("Housing type is required.", presenter.error);
        assertNull(presenter.success);
        assertTrue(applicationDataAccess.getApplicationsByAdopter("adopter1").isEmpty());
    }

    @Test
    void petNoLongerAvailableFails() {
        interactor.execute(input("adopted-cat", "House"));

        assertEquals("This pet is no longer available.", presenter.error);
        assertTrue(applicationDataAccess.getApplicationsByAdopter("adopter1").isEmpty());
    }

    @Test
    void secondActiveApplicationForSamePetFails() {
        interactor.execute(input("mochi", "House"));
        presenter.success = null;
        interactor.execute(input("mochi", "House"));

        assertEquals("You already have an application for this pet.", presenter.error);
        assertNull(presenter.success);
        assertEquals(1, applicationDataAccess.getApplicationsByAdopter("adopter1").size());
    }

    @Test
    void notLoggedInFails() {
        interactor.execute(new SubmitApplicationInputData("", "mochi", "House", "Had two cats", "2 adults",
                "me@example.com", ""));
        assertNotNull(presenter.error);
    }

    @Test
    void saveFailureIsReported() {
        final SubmitApplicationDataAccessInterface failingDataAccess = new SubmitApplicationDataAccessInterface() {
            @Override
            public boolean hasActiveApplication(String adopterId, String petId) {
                return false;
            }

            @Override
            public void save(Application application) {
                throw new RuntimeException("disk full");
            }
        };
        new SubmitApplicationInteractor(failingDataAccess, new FakePetDataAccess(), presenter)
                .execute(input("mochi", "House"));

        assertEquals("The application could not be saved. Please try again.", presenter.error);
        assertNull(presenter.success);
    }

    private static class FakePetDataAccess implements SubmitApplicationPetDataAccessInterface {
        @Override
        public boolean isAvailable(String petId) {
            return "mochi".equals(petId);
        }

        @Override
        public String getPetName(String petId) {
            return "Mochi";
        }
    }

    private static class RecordingPresenter implements SubmitApplicationOutputBoundary {
        private SubmitApplicationOutputData success;
        private String error;

        @Override
        public void prepareSuccessView(SubmitApplicationOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
