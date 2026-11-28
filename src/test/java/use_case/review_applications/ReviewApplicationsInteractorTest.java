package use_case.review_applications;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Application;
import entity.ApplicationStatus;

class ReviewApplicationsInteractorTest {

    private FakeApplicationDataAccess applicationDataAccess;
    private RecordingPresenter presenter;
    private ReviewApplicationsInteractor interactor;

    @BeforeEach
    void setUp() {
        applicationDataAccess = new FakeApplicationDataAccess();
        presenter = new RecordingPresenter();
        interactor = new ReviewApplicationsInteractor(applicationDataAccess, new FakePetDataAccess(),
                new FakeUserDataAccess(), presenter);
    }

    private Application application(String id, String adopterId, String petId) {
        return new Application(id, adopterId, petId, "House", "Had two cats", "2 adults", "me@example.com",
                "Loves naps", LocalDate.of(2026, 11, 16));
    }

    @Test
    void listsEveryApplicationWhenNoFilterIsChosen() {
        applicationDataAccess.applications.add(application("a1", "alice", "mochi"));
        applicationDataAccess.applications.add(application("a2", "bob", "tofu"));

        interactor.execute(new ReviewApplicationsInputData(null, ""));

        final ReviewApplicationsOutputData output = presenter.success;
        assertEquals(2, output.getApplicationCount());
        assertEquals(List.of("a1", "a2"), output.getApplicationIds());
        assertEquals(List.of("Mochi", "Tofu"), output.getPetNames());
        assertEquals(List.of("Alice", "Bob"), output.getAdopterNames());
        assertEquals(List.of("PENDING", "PENDING"), output.getStatuses());
        assertEquals(LocalDate.of(2026, 11, 16), output.getSubmittedDates().get(0));
        assertNull(presenter.error);
    }

    @Test
    void filtersByPetAndStatus() {
        final Application interview = application("a1", "alice", "mochi");
        interview.updateStatus(ApplicationStatus.INTERVIEW, "");
        applicationDataAccess.applications.add(interview);
        applicationDataAccess.applications.add(application("a2", "bob", "mochi"));
        applicationDataAccess.applications.add(application("a3", "bob", "tofu"));

        interactor.execute(new ReviewApplicationsInputData(null, "mochi"));
        assertEquals(List.of("a1", "a2"), presenter.success.getApplicationIds());

        interactor.execute(new ReviewApplicationsInputData(ApplicationStatus.PENDING, null));
        assertEquals(List.of("a2", "a3"), presenter.success.getApplicationIds());

        interactor.execute(new ReviewApplicationsInputData(ApplicationStatus.INTERVIEW, "mochi"));
        assertEquals(List.of("a1"), presenter.success.getApplicationIds());
    }

    @Test
    void reportsWhenNothingMatchesTheFilter() {
        applicationDataAccess.applications.add(application("a1", "alice", "mochi"));

        interactor.execute(new ReviewApplicationsInputData(ApplicationStatus.APPROVED, null));

        assertEquals("No applications match the filter.", presenter.error);
        assertNull(presenter.success);
    }

    @Test
    void reportsWhenThereAreNoApplications() {
        interactor.execute(new ReviewApplicationsInputData(null, null));

        assertEquals("There are no applications.", presenter.error);
        assertNull(presenter.success);
    }

    @Test
    void reportsALoadFailure() {
        applicationDataAccess.failOnRead = true;

        interactor.execute(new ReviewApplicationsInputData(null, null));

        assertEquals("Applications could not be loaded. Please try again.", presenter.error);
        assertNull(presenter.success);
    }

    @Test
    void missingPetOrAdopterShowsAPlaceholder() {
        applicationDataAccess.applications.add(application("a1", "ghost", "removed-cat"));

        interactor.execute(new ReviewApplicationsInputData(null, null));

        assertEquals(List.of("(pet no longer listed)"), presenter.success.getPetNames());
        assertEquals(List.of("(unknown adopter)"), presenter.success.getAdopterNames());
    }

    @Test
    void showsTheDetailsOfOneApplication() {
        final Application application = application("a1", "alice", "mochi");
        application.updateStatus(ApplicationStatus.INTERVIEW, "Call us on Friday");
        applicationDataAccess.applications.add(application);

        interactor.viewApplication("a1");

        final ApplicationDetailsOutputData details = presenter.details;
        assertEquals("a1", details.getApplicationId());
        assertEquals("mochi", details.getPetId());
        assertEquals("Mochi", details.getPetName());
        assertEquals("Alice", details.getAdopterName());
        assertEquals(LocalDate.of(2026, 11, 16), details.getSubmittedAt());
        assertEquals("INTERVIEW", details.getStatus());
        assertEquals("House", details.getHousingType());
        assertEquals("Had two cats", details.getPetExperience());
        assertEquals("2 adults", details.getHouseholdMembers());
        assertEquals("me@example.com", details.getContactInfo());
        assertEquals("Loves naps", details.getNote());
        assertEquals("Call us on Friday", details.getAdminNote());
        assertNull(presenter.detailError);
    }

    @Test
    void detailFailsWithoutAChoiceOrForAMissingApplication() {
        interactor.viewApplication("");
        assertEquals("Please choose an application.", presenter.detailError);

        interactor.viewApplication(null);
        assertEquals("Please choose an application.", presenter.detailError);

        interactor.viewApplication("does-not-exist");
        assertEquals("This application no longer exists.", presenter.detailError);

        assertNull(presenter.details);
    }

    @Test
    void detailReportsALoadFailure() {
        applicationDataAccess.failOnRead = true;

        interactor.viewApplication("a1");

        assertEquals("Application could not be loaded. Please try again.", presenter.detailError);
        assertNull(presenter.details);
    }

    @Test
    void outputDataRejectsListsOfDifferentLengths() {
        final IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new ReviewApplicationsOutputData(List.of("a1"), List.of(), List.of("Alice"),
                        List.of(LocalDate.of(2026, 11, 16)), List.of("PENDING")));

        assertEquals("Every list must have one entry per application.", error.getMessage());
    }

    @Test
    void switchesToTheUpdateApplicationView() {
        interactor.switchToUpdateApplicationView();

        assertTrue(presenter.switchedToUpdate);
    }

    private static final class FakeApplicationDataAccess implements ReviewApplicationsDataAccessInterface {
        private final List<Application> applications = new ArrayList<>();
        private boolean failOnRead;

        @Override
        public List<Application> getAllApplications() {
            if (failOnRead) {
                throw new RuntimeException("file damaged");
            }
            return new ArrayList<>(applications);
        }

        @Override
        public Application getApplicationById(String applicationId) {
            if (failOnRead) {
                throw new RuntimeException("file damaged");
            }
            for (Application application : applications) {
                if (application.getId().equals(applicationId)) {
                    return application;
                }
            }
            return null;
        }
    }

    private static final class FakePetDataAccess implements ReviewApplicationsPetDataAccessInterface {
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

    private static final class FakeUserDataAccess implements ReviewApplicationsUserDataAccessInterface {
        @Override
        public String getUserName(String userId) {
            if ("alice".equals(userId)) {
                return "Alice";
            }
            if ("bob".equals(userId)) {
                return "Bob";
            }
            return null;
        }
    }

    private static final class RecordingPresenter implements ReviewApplicationsOutputBoundary {
        private ReviewApplicationsOutputData success;
        private ApplicationDetailsOutputData details;
        private String error;
        private String detailError;
        private boolean switchedToUpdate;

        @Override
        public void prepareSuccessView(ReviewApplicationsOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareDetailView(ApplicationDetailsOutputData outputData) {
            details = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }

        @Override
        public void prepareDetailFailView(String errorMessage) {
            detailError = errorMessage;
        }

        @Override
        public void switchToUpdateApplicationView() {
            switchedToUpdate = true;
        }
    }
}
