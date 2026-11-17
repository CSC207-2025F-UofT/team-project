package use_case.enrol_course;

import entity.Course;
import entity.CourseFactory;
import entity.LetterGrade;
import entity.Topic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * The Enrol Course Interactor. It checks the details the user typed, rejects a
 * course the user is already enrolled in, and saves a new Course.
 */
public class EnrolCourseInteractor implements EnrolCourseInputBoundary {

    private final EnrolCourseDataAccessInterface courseDataAccessObject;
    private final EnrolCourseOutputBoundary enrolPresenter;
    private final CourseFactory courseFactory;

    public EnrolCourseInteractor(EnrolCourseDataAccessInterface courseDataAccessObject,
                                 EnrolCourseOutputBoundary enrolPresenter,
                                 CourseFactory courseFactory) {
        this.courseDataAccessObject = courseDataAccessObject;
        this.enrolPresenter = enrolPresenter;
        this.courseFactory = courseFactory;
    }

    @Override
    public void execute(EnrolCourseInputData inputData) {
        final String username = inputData.getUsername();
        final String code = normalise(inputData.getCourseCode()).toUpperCase(Locale.ROOT);
        final String name = normalise(inputData.getCourseName());
        final List<String> topicNames = cleanTopicNames(inputData.getTopicNames());

        if (username == null || username.isEmpty()) {
            enrolPresenter.prepareFailView("You must be logged in to enrol in a course.");
        }
        else if (code.isEmpty()) {
            enrolPresenter.prepareFailView("Course code cannot be empty.");
        }
        else if (name.isEmpty()) {
            enrolPresenter.prepareFailView("Course name cannot be empty.");
        }
        else if (!isKnownGrade(inputData.getTargetGrade())) {
            enrolPresenter.prepareFailView("Choose a target grade such as A, B+ or C.");
        }
        else if (topicNames.isEmpty()) {
            enrolPresenter.prepareFailView("Enter at least one topic.");
        }
        else if (findDuplicate(topicNames) != null) {
            enrolPresenter.prepareFailView("Topic listed twice: " + findDuplicate(topicNames));
        }
        else if (courseDataAccessObject.existsByCode(username, code)) {
            enrolPresenter.prepareFailView("You are already enrolled in " + code + ".");
        }
        else {
            final LetterGrade targetGrade = LetterGrade.fromLabel(inputData.getTargetGrade());
            final Course course = courseFactory.create(code, name, targetGrade, topicNames);
            courseDataAccessObject.saveCourse(username, course);

            final List<String> savedTopics = new ArrayList<>();
            for (Topic topic : course.getTopics()) {
                savedTopics.add(topic.getName());
            }
            enrolPresenter.prepareSuccessView(new EnrolCourseOutputData(username,
                    course.getCode(), course.getName(), targetGrade.getLabel(), savedTopics));
        }
    }

    @Override
    public void switchToEnrolView() {
        enrolPresenter.switchToEnrolView();
    }

    @Override
    public void switchToHomeView() {
        enrolPresenter.switchToHomeView();
    }

    private static String normalise(String text) {
        if (text == null) {
            return "";
        }
        return text.trim();
    }

    /**
     * Trims every topic name and drops the blank ones, so stray empty lines are ignored.
     */
    private static List<String> cleanTopicNames(List<String> rawNames) {
        final List<String> cleaned = new ArrayList<>();
        for (String rawName : rawNames) {
            final String topicName = normalise(rawName);
            if (!topicName.isEmpty()) {
                cleaned.add(topicName);
            }
        }
        return cleaned;
    }

    /**
     * Returns the first topic name that appears twice, ignoring case, or null if there is none.
     */
    private static String findDuplicate(List<String> topicNames) {
        final Set<String> seen = new HashSet<>();
        for (String topicName : topicNames) {
            if (!seen.add(topicName.toLowerCase(Locale.ROOT))) {
                return topicName;
            }
        }
        return null;
    }

    private static boolean isKnownGrade(String label) {
        try {
            LetterGrade.fromLabel(label);
            return true;
        }
        catch (IllegalArgumentException notAGrade) {
            return false;
        }
    }
}
