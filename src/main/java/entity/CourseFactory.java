package entity;

import java.util.List;

/**
 * Factory for creating Course objects.
 */
public class CourseFactory {

    /**
     * Creates a course with no topics.
     * @param code the course code
     * @param name the course name
     * @param targetGrade the grade the student is aiming for
     * @return the new course
     */
    public Course create(String code, String name, LetterGrade targetGrade) {
        return new Course(code, name, targetGrade);
    }

    /**
     * Creates a newly enrolled course. Every topic starts as RED, since the
     * student has not rated it yet.
     * @param code the course code
     * @param name the course name
     * @param targetGrade the grade the student is aiming for
     * @param topicNames the names of the main topics of the course
     * @return the new course
     */
    public Course create(String code, String name, LetterGrade targetGrade, List<String> topicNames) {
        final Course course = new Course(code, name, targetGrade);
        for (String topicName : topicNames) {
            course.addTopic(new Topic(topicName, ConfidenceLevel.RED));
        }
        return course;
    }
}
