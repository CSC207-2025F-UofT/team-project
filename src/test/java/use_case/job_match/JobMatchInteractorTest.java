package use_case.job_match;

import entity.Job;
import entity.Student;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class JobMatchInteractorTest {

    private static Student makeStudent() {
        ArrayList<String> courses = new ArrayList<>();
        courses.add("CSC207");
        ArrayList<String> skills = new ArrayList<>();
        skills.add("Java");
        return new Student("Paul", "password", courses, skills);
    }

    private static Job makeJob(String title) {
        ArrayList<String> skills = new ArrayList<>();
        skills.add("Java");
        return new Job(title, skills, "Google", "2026-11-20", "Internship",
                "Toronto", "$30/hour", "https://example.com");
    }

    /** Student DAO that only knows about the given student. */
    private static JobMatchStudentDataAccessInterface studentRepo(Student student) {
        return username -> student != null && student.getName().equals(username) ? student : null;
    }

    /** Presenter that records what it was asked to show. */
    private static class RecordingPresenter implements JobMatchOutputBoundary {
        JobMatchOutputData successData;
        String failMessage;
        boolean switchedToLoggedIn;

        @Override
        public void prepareSuccessView(JobMatchOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failMessage = errorMessage;
        }

        @Override
        public void switchToLoggedInView() {
            switchedToLoggedIn = true;
        }
    }

    @Test
    public void successTest() {
        Student student = makeStudent();
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(makeJob("Software Developer"));
        jobs.add(makeJob("Backend Intern"));

        final Student[] studentPassedToJobDao = new Student[1];
        JobMatchUserDataAccessInterface jobRepo = s -> {
            studentPassedToJobDao[0] = s;
            return jobs;
        };

        RecordingPresenter presenter = new RecordingPresenter();
        JobMatchInputBoundary interactor = new JobMatchInteractor(jobRepo, studentRepo(student), presenter);

        interactor.execute(new JobMatchInputData("Paul"));

        assertNull("Use case failure is unexpected.", presenter.failMessage);
        assertNotNull("Success view should be prepared.", presenter.successData);
        assertSame(student, studentPassedToJobDao[0]);
        assertEquals(2, presenter.successData.getJobs().size());
        assertEquals("Software Developer", presenter.successData.getJobs().get(0).getTitle());
        assertEquals("Backend Intern", presenter.successData.getJobs().get(1).getTitle());
    }

    @Test
    public void failureStudentNotFoundTest() {
        JobMatchUserDataAccessInterface jobRepo = s -> {
            fail("Jobs should not be fetched when the student does not exist.");
            return null;
        };

        RecordingPresenter presenter = new RecordingPresenter();
        JobMatchInputBoundary interactor = new JobMatchInteractor(jobRepo, studentRepo(null), presenter);

        interactor.execute(new JobMatchInputData("Nobody"));

        assertNull("Use case success is unexpected.", presenter.successData);
        assertEquals("Please log in first.", presenter.failMessage);
    }

    @Test
    public void failureJobFetchExceptionTest() {
        JobMatchUserDataAccessInterface jobRepo = s -> {
            throw new JobFetchException("Could not reach the job API.");
        };

        RecordingPresenter presenter = new RecordingPresenter();
        JobMatchInputBoundary interactor = new JobMatchInteractor(jobRepo, studentRepo(makeStudent()), presenter);

        interactor.execute(new JobMatchInputData("Paul"));

        assertNull("Use case success is unexpected.", presenter.successData);
        assertEquals("Could not reach the job API.", presenter.failMessage);
    }

    @Test
    public void failureNoMatchingJobsTest() {
        JobMatchUserDataAccessInterface jobRepo = s -> new ArrayList<>();

        RecordingPresenter presenter = new RecordingPresenter();
        JobMatchInputBoundary interactor = new JobMatchInteractor(jobRepo, studentRepo(makeStudent()), presenter);

        interactor.execute(new JobMatchInputData("Paul"));

        assertNull("Use case success is unexpected.", presenter.successData);
        assertEquals("No matching jobs found.", presenter.failMessage);
    }

    @Test
    public void switchToLoggedInViewTest() {
        JobMatchUserDataAccessInterface jobRepo = s -> {
            fail("Jobs should not be fetched when switching views.");
            return null;
        };

        RecordingPresenter presenter = new RecordingPresenter();
        JobMatchInputBoundary interactor = new JobMatchInteractor(jobRepo, studentRepo(makeStudent()), presenter);

        interactor.switchToLoggedInView();

        assertTrue(presenter.switchedToLoggedIn);
        assertNull(presenter.successData);
        assertNull(presenter.failMessage);
    }
}
