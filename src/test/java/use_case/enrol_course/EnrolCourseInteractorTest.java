package use_case.enrol_course;

import data_access.InMemoryCourseDataAccessObject;
import entity.ConfidenceLevel;
import entity.Course;
import entity.CourseFactory;
import entity.LetterGrade;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class EnrolCourseInteractorTest {

    private static final String USER = "Paul";

    /**
     * A presenter that records what the interactor told it, so each test can assert on it afterwards.
     * (Asserting afterwards also catches the case where the presenter is never called.)
     */
    private static class RecordingPresenter implements EnrolCourseOutputBoundary {
        private EnrolCourseOutputData success;
        private String error;
        private String switchedTo;

        @Override
        public void prepareSuccessView(EnrolCourseOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }

        @Override
        public void switchToEnrolView() {
            switchedTo = "enrol";
        }

        @Override
        public void switchToHomeView() {
            switchedTo = "home";
        }
    }

    private final InMemoryCourseDataAccessObject courseRepository = new InMemoryCourseDataAccessObject();
    private final RecordingPresenter presenter = new RecordingPresenter();
    private final EnrolCourseInputBoundary interactor =
            new EnrolCourseInteractor(courseRepository, presenter, new CourseFactory());

    private void enrol(String code, String name, String grade, List<String> topics) {
        interactor.execute(new EnrolCourseInputData(USER, code, name, grade, topics));
    }

    private void assertFailsWith(String expectedError) {
        if (presenter.success != null) {
            fail("Use case success is unexpected.");
        }
        assertEquals(expectedError, presenter.error);
    }

    @Test
    void successSavesCourseWithRedTopics() {
        enrol(" csc207 ", " Software Design ", "A-", Arrays.asList("SOLID", " Clean Architecture ", ""));

        assertNotNull(presenter.success, "Use case failure is unexpected: " + presenter.error);
        assertEquals(USER, presenter.success.getUsername());
        assertEquals("CSC207", presenter.success.getCourseCode());
        assertEquals("Software Design", presenter.success.getCourseName());
        assertEquals("A-", presenter.success.getTargetGrade());
        assertEquals(Arrays.asList("SOLID", "Clean Architecture"), presenter.success.getTopicNames());

        final Course saved = courseRepository.getCourse(USER, "CSC207");
        assertNotNull(saved);
        assertEquals(LetterGrade.A_MINUS, saved.getTargetGrade());
        assertEquals(2, saved.getTopics().size());
        assertEquals(ConfidenceLevel.RED, saved.getTopics().get(0).getConfidence());
    }

    @Test
    void duplicateCourseIsRejectedIgnoringCase() {
        enrol("CSC207", "Software Design", "A", Collections.singletonList("SOLID"));
        presenter.success = null;

        enrol("csc207", "Another Name", "B", Collections.singletonList("Generics"));

        assertFailsWith("You are already enrolled in CSC207.");
        assertEquals("Software Design", courseRepository.getCourse(USER, "CSC207").getName());
    }

    @Test
    void sameCodeForDifferentUserIsAllowed() {
        enrol("CSC207", "Software Design", "A", Collections.singletonList("SOLID"));
        interactor.execute(new EnrolCourseInputData("Alice", "CSC207", "Software Design", "B",
                Collections.singletonList("SOLID")));

        assertNotNull(courseRepository.getCourse("Alice", "CSC207"));
        assertEquals(1, courseRepository.getCourses(USER).size());
    }

    @Test
    void emptyCodeIsRejected() {
        enrol("  ", "Software Design", "A", Collections.singletonList("SOLID"));
        assertFailsWith("Course code cannot be empty.");
    }

    @Test
    void emptyNameIsRejected() {
        enrol("CSC207", "", "A", Collections.singletonList("SOLID"));
        assertFailsWith("Course name cannot be empty.");
    }

    @Test
    void unknownTargetGradeIsRejected() {
        enrol("CSC207", "Software Design", "Z", Collections.singletonList("SOLID"));
        assertFailsWith("Choose a target grade such as A, B+ or C.");
    }

    @Test
    void missingTopicsAreRejected() {
        enrol("CSC207", "Software Design", "A", Arrays.asList(" ", ""));
        assertFailsWith("Enter at least one topic.");
    }

    @Test
    void duplicateTopicsAreRejected() {
        enrol("CSC207", "Software Design", "A", Arrays.asList("SOLID", "solid"));
        assertFailsWith("Topic listed twice: solid");
        assertTrue(courseRepository.getCourses(USER).isEmpty());
    }

    @Test
    void missingUsernameIsRejected() {
        interactor.execute(new EnrolCourseInputData("", "CSC207", "Software Design", "A",
                Collections.singletonList("SOLID")));
        assertFailsWith("You must be logged in to enrol in a course.");
    }

    @Test
    void switchViews() {
        interactor.switchToEnrolView();
        assertEquals("enrol", presenter.switchedTo);
        interactor.switchToHomeView();
        assertEquals("home", presenter.switchedTo);
    }
}
