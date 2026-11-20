package use_case.job_grouping;

import entity.Job;
import entity.JobGroup;
import entity.Student;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CreateJobGroupInteractorTest {

    @Test
    public void successTest() {
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job("Software Developer", new ArrayList<>(),
                "Google", "2026-11-20", "Internship",
                "Toronto", "$30/hour", "https://example.com"));

        CreateJobGroupInputData inputData =
                new CreateJobGroupInputData("Paul", "Summer Internships", jobs);

        ArrayList<String> courses = new ArrayList<>();
        courses.add("CSC207");

        ArrayList<String> skills = new ArrayList<>();
        skills.add("Java");

        Student student = new Student("Paul", "password", courses, skills);

        // Create a fake repository for this test.
        CreateJobGroupDataAccessInterface userRepository =
                new CreateJobGroupDataAccessInterface() {

                    @Override
                    public Student getStudent(String username) {
                        if ("Paul".equals(username)) {
                            return student;
                        }
                        return null;
                    }

                    @Override
                    public void save(Student updatedStudent) {
                        assertEquals(student, updatedStudent);
                    }
                };

        CreateJobGroupOutputBoundary successPresenter =
                new CreateJobGroupOutputBoundary() {

                    @Override
                    public void prepareSuccessView(CreateJobGroupOutputData outputData) {
                        assertEquals("Summer Internships", outputData.getGroupName());
                        assertEquals(1, student.getJobGroups().size());

                        JobGroup group = student.getJobGroups().get(0);
                        assertEquals("Summer Internships", group.getName());
                        assertEquals(1, group.getJobs().size());
                        assertEquals("Software Developer",
                                group.getJobs().get(0).getTitle());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        fail("Use case failure is unexpected.");
                    }
                };

        CreateJobGroupInputBoundary interactor =
                new CreateJobGroupInteractor(userRepository, successPresenter);

        interactor.execute(inputData);
    }

    @Test
    public void failureEmptyNameTest() {
        CreateJobGroupInputData inputData =
                new CreateJobGroupInputData("Paul", "", new ArrayList<>());

        CreateJobGroupDataAccessInterface userRepository =
                new CreateJobGroupDataAccessInterface() {

                    @Override
                    public Student getStudent(String username) {
                        return null;
                    }

                    @Override
                    public void save(Student student) {
                        fail("Student should not be saved.");
                    }
                };

        CreateJobGroupOutputBoundary failurePresenter =
                new CreateJobGroupOutputBoundary() {

                    @Override
                    public void prepareSuccessView(CreateJobGroupOutputData outputData) {
                        fail("Use case success is unexpected.");
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        assertEquals("Group name cannot be empty", errorMessage);
                    }
                };

        CreateJobGroupInputBoundary interactor =
                new CreateJobGroupInteractor(userRepository, failurePresenter);

        interactor.execute(inputData);
    }
}
