package use_case.track_applications;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Application;
import entity.ApplicationStatus;

class TrackApplicationsInteractorTest {

    private FakeApplicationDataAccess applicationDataAccess;
    private RecordingPresenter presenter;
    private TrackApplicationsInteractor interactor;

    @BeforeEach
    void setUp() {
        applicationDataAccess = new FakeApplicationDataAccess();
        presenter = new RecordingPresenter();
        interactor = new TrackApplicationsInteractor(applicationDataAccess, new FakePetDataAccess(), presenter);
    }

    private Application application(String id, String adopterId, String petId) {
        return new Application(id, adopterId, petId, "House", "Had two cats", "2 adults", "me@example.com",
                "", LocalDate.of(2026, 11, 16));
    }

    @Test
    void listsOnlyTheAdoptersApplicationsWithStatusAndNote() {
        final Application reviewed = application("a1", "adopter1", "mochi");
        reviewed.updateStatus(ApplicationStatus.INTERVIEW, "Call us on Friday");
        applicationDataAccess.applications.add(reviewed);
        applicationDataAccess.applications.add(application("a2", "adopter1", "tofu"));
        applicationDataAccess.applications.add(application("a3", "someone-else", "mochi"));

        interactor.execute(new TrackApplicationsInputData("adopter1"));

        final List<ApplicationSummary> summaries = presenter.success.getApplications();
        assertEquals(2, summaries.size());
        assertEquals("Mochi", summaries.get(0).getPetName());
        assertEquals("INTERVIEW", summaries.get(0).getStatus());
        assertEquals("Call us on Friday", summaries.get(0).getAdminNote());
        assertEquals(LocalDate.of(2026, 11, 16), summaries.get(0).getSubmittedAt());
        assertEquals("Tofu", summaries.get(1).getPetName());
        assertEquals("PENDING", summaries.get(1).getStatus());
    }

    @Test
    void noApplicationsGivesEmptyList() {
        interactor.execute(new TrackApplicationsInputData("adopter1"));

        assertTrue(presenter.success.getApplications().isEmpty());
        assertNull(presenter.error);
    }

    @Test
    void notLoggedInFails() {
        interactor.execute(new TrackApplicationsInputData(""));

        assertNotNull(presenter.error);
        assertNull(presenter.success);
    }

    @Test
    void removedPetStillShowsTheApplication() {
        applicationDataAccess.applications.add(application("a1", "adopter1", "removed-cat"));

        interactor.execute(new TrackApplicationsInputData("adopter1"));

        assertEquals(1, presenter.success.getApplications().size());
        assertEquals(TrackApplicationsInteractor.UNKNOWN_PET, presenter.success.getApplications().get(0).getPetName());
    }

    @Test
    void loadFailureIsReported() {
        applicationDataAccess.failOnRead = true;

        interactor.execute(new TrackApplicationsInputData("adopter1"));

        assertEquals("Your applications could not be loaded. Please try again.", presenter.error);
        assertNull(presenter.success);
    }

    private static class FakeApplicationDataAccess implements TrackApplicationsDataAccessInterface {
        private final List<Application> applications = new ArrayList<>();
        private boolean failOnRead;

        @Override
        public List<Application> getApplicationsByAdopter(String adopterId) {
            if (failOnRead) {
                throw new RuntimeException("file damaged");
            }
            final List<Application> result = new ArrayList<>();
            for (Application application : applications) {
                if (application.getAdopterId().equals(adopterId)) {
                    result.add(application);
                }
            }
            return result;
        }
    }

    private static class FakePetDataAccess implements TrackApplicationsPetDataAccessInterface {
        @Override
        public String getPetName(String petId) {
            if ("mochi".equals(petId)) {
                return "Mochi";
            }
            if ("tofu".equals(petId)) {
                return "Tofu";
            }
            throw new IllegalArgumentException("Pet does not exist: " + petId);
        }
    }

    private static class RecordingPresenter implements TrackApplicationsOutputBoundary {
        private TrackApplicationsOutputData success;
        private String error;

        @Override
        public void prepareSuccessView(TrackApplicationsOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
