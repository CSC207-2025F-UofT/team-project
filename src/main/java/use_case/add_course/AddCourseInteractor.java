package use_case.add_course;

import java.util.ArrayList;
import java.util.List;

import entity.Student;
import entity.StudentFactory;

/**
 * The Interactor for the Add Course Use Case.
 */
public class AddCourseInteractor implements AddCourseInputBoundary {

    private final AddCourseDataAccessInterface studentDataAccessObject;
    private final CourseValidationGateway courseValidationGateway;
    private final AddCourseOutputBoundary addCoursePresenter;
    private final StudentFactory studentFactory;

    public AddCourseInteractor(AddCourseDataAccessInterface studentDataAccessObject,
                               CourseValidationGateway courseValidationGateway,
                               AddCourseOutputBoundary addCoursePresenter,
                               StudentFactory studentFactory) {
        this.studentDataAccessObject = studentDataAccessObject;
        this.courseValidationGateway = courseValidationGateway;
        this.addCoursePresenter = addCoursePresenter;
        this.studentFactory = studentFactory;
    }

    @Override
    public void execute(AddCourseInputData addCourseInputData) {
        final String username = studentDataAccessObject.getCurrentUsername();
        final Student student = studentDataAccessObject.get(username);
        final ArrayList<String> currentCourses = student.getCourses();

        final List<String> addedCourses = new ArrayList<>();
        final List<String> rejectedCourses = new ArrayList<>();

        for (String rawCode : addCourseInputData.getRawCourseCodes()) {
            final String code = CourseCodeFormat.normalize(rawCode);
            if (code.isEmpty()) {
                continue;
            }
            if (!CourseCodeFormat.isValidFormat(code)) {
                rejectedCourses.add(code + " (invalid format)");
            }
            else if (currentCourses.contains(code) || addedCourses.contains(code)) {
                rejectedCourses.add(code + " (already added)");
            }
            else {
                try {
                    if (courseValidationGateway.courseExists(code)) {
                        addedCourses.add(code);
                    }
                    else {
                        rejectedCourses.add(code + " (not a UofT course)");
                    }
                }
                catch (CourseLookupException e) {
                    addCoursePresenter.prepareFailView(
                            "Could not check courses right now. Please try again.");
                    return;
                }
            }
        }

        if (addedCourses.isEmpty()) {
            if (rejectedCourses.isEmpty()) {
                addCoursePresenter.prepareFailView("Please enter at least one course code.");
            }
            else {
                addCoursePresenter.prepareFailView(
                        "No courses were added: " + String.join(", ", rejectedCourses));
            }
            return;
        }

        final ArrayList<String> allCourses = new ArrayList<>(currentCourses);
        allCourses.addAll(addedCourses);

        final Student updatedStudent = studentFactory.create(
                student.getName(), student.getPassword(), allCourses, student.getSkills());
        studentDataAccessObject.save(updatedStudent);

        addCoursePresenter.prepareSuccessView(
                new AddCourseOutputData(addedCourses, rejectedCourses, allCourses));
    }
}