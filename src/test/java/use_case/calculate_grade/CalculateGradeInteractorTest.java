package use_case.calculate_grade;

import data_access.InMemoryCourseDataAccessObject;
import entity.Course;
import entity.CourseFactory;
import entity.LetterGrade;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculateGradeInteractorTest {

    private static final double DELTA = 1e-6;

    private InMemoryCourseDataAccessObject repositoryWithCourse() {
        final InMemoryCourseDataAccessObject repository = new InMemoryCourseDataAccessObject();
        final Course course = new CourseFactory().create("CSC207", "Software Design", LetterGrade.A_MINUS,
                Arrays.asList("SOLID", "Clean Architecture"));
        repository.saveCourse("Paul", course);
        return repository;
    }

    private static CalculateGradeInputData.AssessmentRow row(String name, String mark, String weight) {
        return new CalculateGradeInputData.AssessmentRow(name, mark, weight);
    }

    private static CalculateGradeOutputBoundary expectFailure(String expectedError) {
        return new CalculateGradeOutputBoundary() {
            @Override
            public void prepareSuccessView(CalculateGradeOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        };
    }

    @Test
    void successTest() {
        final InMemoryCourseDataAccessObject repository = repositoryWithCourse();
        final CalculateGradeInputData inputData = new CalculateGradeInputData("Paul", "csc207", Arrays.asList(
                row("Midterm", "80", "20"),
                row("Assignment 1", " 90 ", "10")));
        final boolean[] called = {false};

        final CalculateGradeOutputBoundary successPresenter = new CalculateGradeOutputBoundary() {
            @Override
            public void prepareSuccessView(CalculateGradeOutputData outputData) {
                called[0] = true;
                // (80 * 20 + 90 * 10) / 30 = 83.33...
                assertEquals(83.333333, outputData.getCurrentPercentage(), DELTA);
                assertTrue(outputData.hasMarks());
                assertEquals("A-", outputData.getEstimatedGrade());
                assertTrue(outputData.isTargetReached());
                assertEquals(30, outputData.getCompletedWeight(), DELTA);
                assertEquals("A-", outputData.getTargetGrade());
                assertFalse(outputData.isCourseCompleted());
                // (80 * 100 - 2500) / 70 = 78.57...
                assertEquals(78.571428, outputData.getRequiredAverage(), DELTA);
                assertEquals(Arrays.asList("Midterm", "Assignment 1"), outputData.getAssessmentNames());

                final Course saved = repository.getCourse("Paul", "CSC207");
                assertEquals(2, saved.getAssessments().size());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };

        new CalculateGradeInteractor(repository, successPresenter).execute(inputData);
        assertTrue(called[0]);
    }

    @Test
    void recalculatingReplacesOldAssessmentsTest() {
        final InMemoryCourseDataAccessObject repository = repositoryWithCourse();
        final CalculateGradeOutputBoundary quietSuccess = new CalculateGradeOutputBoundary() {
            @Override
            public void prepareSuccessView(CalculateGradeOutputData outputData) {
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };
        final CalculateGradeInteractor interactor = new CalculateGradeInteractor(repository, quietSuccess);
        interactor.execute(new CalculateGradeInputData("Paul", "CSC207",
                Collections.singletonList(row("Midterm", "50", "60"))));
        interactor.execute(new CalculateGradeInputData("Paul", "CSC207",
                Arrays.asList(row("Quiz", "100", "50"), row("Final", "100", "50"))));

        final Course saved = repository.getCourse("Paul", "CSC207");
        assertEquals(2, saved.getAssessments().size());
        assertEquals(100, saved.getCompletedWeight(), DELTA);
    }

    @Test
    void courseCompletedTest() {
        final InMemoryCourseDataAccessObject repository = repositoryWithCourse();
        final boolean[] called = {false};
        final CalculateGradeOutputBoundary presenter = new CalculateGradeOutputBoundary() {
            @Override
            public void prepareSuccessView(CalculateGradeOutputData outputData) {
                called[0] = true;
                assertTrue(outputData.isCourseCompleted());
                assertEquals("B", outputData.getEstimatedGrade());
                // 73.3% is below the A- minimum of 80%.
                assertFalse(outputData.isTargetReached());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };
        new CalculateGradeInteractor(repository, presenter).execute(new CalculateGradeInputData("Paul", "CSC207",
                Arrays.asList(row("Term work", "70", "33.3"), row("Midterm", "75", "33.3"),
                        row("Final", "75", "33.4"))));
        assertTrue(called[0]);
    }

    @Test
    void percentSignsAreAcceptedTest() {
        final InMemoryCourseDataAccessObject repository = repositoryWithCourse();
        final boolean[] called = {false};
        final CalculateGradeOutputBoundary presenter = new CalculateGradeOutputBoundary() {
            @Override
            public void prepareSuccessView(CalculateGradeOutputData outputData) {
                called[0] = true;
                assertEquals(80, outputData.getCurrentPercentage(), DELTA);
                assertEquals(25, outputData.getCompletedWeight(), DELTA);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };
        new CalculateGradeInteractor(repository, presenter).execute(new CalculateGradeInputData("Paul", "CSC207",
                Collections.singletonList(row("Midterm", "80%", " 25 % "))));
        assertTrue(called[0]);
    }

    @Test
    void failureCourseNotFoundTest() {
        final CalculateGradeInputData inputData = new CalculateGradeInputData("Paul", "MAT137",
                Collections.singletonList(row("Midterm", "80", "20")));
        new CalculateGradeInteractor(repositoryWithCourse(), expectFailure("You are not enrolled in MAT137."))
                .execute(inputData);
    }

    @Test
    void noRowsClearsSavedAssessmentsTest() {
        final InMemoryCourseDataAccessObject repository = repositoryWithCourse();
        repository.getCourse("Paul", "CSC207").setAssessments(Collections.singletonList(
                new entity.Assessment("Midterm", 80, 20)));
        final boolean[] called = {false};
        final CalculateGradeOutputBoundary presenter = new CalculateGradeOutputBoundary() {
            @Override
            public void prepareSuccessView(CalculateGradeOutputData outputData) {
                called[0] = true;
                assertFalse(outputData.hasMarks());
                assertEquals("", outputData.getEstimatedGrade());
                assertEquals(0, outputData.getCompletedWeight(), DELTA);
                assertFalse(outputData.isTargetReached());
                // A- needs 80 on the whole course.
                assertEquals(80, outputData.getRequiredAverage(), DELTA);
                assertTrue(outputData.getAssessmentNames().isEmpty());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        };
        new CalculateGradeInteractor(repository, presenter).execute(
                new CalculateGradeInputData("Paul", "CSC207", Collections.emptyList()));
        assertTrue(called[0]);
        assertTrue(repository.getCourse("Paul", "CSC207").getAssessments().isEmpty());
    }

    @Test
    void failureMarkNotANumberTest() {
        final CalculateGradeInputData inputData = new CalculateGradeInputData("Paul", "CSC207", Arrays.asList(
                row("Midterm", "80", "20"),
                row("Quiz", "eighty", "5")));
        new CalculateGradeInteractor(repositoryWithCourse(), expectFailure("Row 2: Mark must be a number."))
                .execute(inputData);
    }

    @Test
    void failureEmptyWeightTest() {
        final CalculateGradeInputData inputData = new CalculateGradeInputData("Paul", "CSC207",
                Collections.singletonList(row("Midterm", "80", " ")));
        new CalculateGradeInteractor(repositoryWithCourse(), expectFailure("Row 1: Weight cannot be empty."))
                .execute(inputData);
    }

    @Test
    void failureMarkOutOfRangeTest() {
        final CalculateGradeInputData inputData = new CalculateGradeInputData("Paul", "CSC207",
                Collections.singletonList(row("Midterm", "105", "20")));
        new CalculateGradeInteractor(repositoryWithCourse(),
                expectFailure("Row 1: Mark must be between 0 and 100.")).execute(inputData);
    }

    @Test
    void failureEmptyNameTest() {
        final CalculateGradeInputData inputData = new CalculateGradeInputData("Paul", "CSC207",
                Collections.singletonList(row("", "80", "20")));
        new CalculateGradeInteractor(repositoryWithCourse(),
                expectFailure("Row 1: Assessment name cannot be empty.")).execute(inputData);
    }

    @Test
    void failureDuplicateNameTest() {
        final CalculateGradeInputData inputData = new CalculateGradeInputData("Paul", "CSC207", Arrays.asList(
                row("Midterm", "80", "20"),
                row("midterm", "70", "20")));
        new CalculateGradeInteractor(repositoryWithCourse(),
                expectFailure("Row 2: \"midterm\" is entered more than once.")).execute(inputData);
    }

    @Test
    void failureWeightsOver100KeepsOldAssessmentsTest() {
        final InMemoryCourseDataAccessObject repository = repositoryWithCourse();
        final List<CalculateGradeInputData.AssessmentRow> rows = Arrays.asList(
                row("Midterm", "80", "60"),
                row("Final", "70", "50"));
        new CalculateGradeInteractor(repository,
                expectFailure("Assessment weights cannot add up to more than 100."))
                .execute(new CalculateGradeInputData("Paul", "CSC207", rows));
        assertTrue(repository.getCourse("Paul", "CSC207").getAssessments().isEmpty());
    }
}
