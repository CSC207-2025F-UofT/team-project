package entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseTest {

    private static final double DELTA = 1e-6;

    private Course newCourse() {
        return new CourseFactory().create("CSC207", "Software Design", LetterGrade.A_MINUS,
                Arrays.asList("SOLID", "Clean Architecture"));
    }

    @Test
    void newCourseHasRedTopicsAndNoGrade() {
        final Course course = newCourse();
        assertEquals(2, course.getTopics().size());
        assertEquals(ConfidenceLevel.RED, course.getTopics().get(0).getConfidence());
        assertFalse(course.getCurrentPercentage().isPresent());
        assertFalse(course.getEstimatedGrade().isPresent());
    }

    @Test
    void emptyCodeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Course(" ", "Software Design", LetterGrade.A));
    }

    @Test
    void currentPercentageIsWeightedAverage() {
        final Course course = newCourse();
        course.setAssessments(Arrays.asList(
                new Assessment("Midterm", 70, 20),
                new Assessment("Assignment", 90, 10)));
        // (70 * 20 + 90 * 10) / 30
        assertEquals(76.666666, course.getCurrentPercentage().getAsDouble(), DELTA);
        assertEquals(LetterGrade.B, course.getEstimatedGrade().get());
        assertEquals(30, course.getCompletedWeight(), DELTA);
    }

    @Test
    void weightsOverOneHundredAreRejected() {
        final Course course = newCourse();
        assertThrows(IllegalArgumentException.class, () -> course.setAssessments(Arrays.asList(
                new Assessment("Midterm", 70, 60),
                new Assessment("Exam", 80, 50))));
        assertTrue(course.getAssessments().isEmpty());
    }

    @Test
    void requiredAverageForTarget() {
        final Course course = newCourse();
        course.setAssessments(Collections.singletonList(new Assessment("Midterm", 70, 40)));
        // need 80 overall: (80 * 100 - 70 * 40) / 60
        assertEquals(86.666666, course.getRequiredAverageForTarget().getAsDouble(), DELTA);
    }

    @Test
    void requiredAverageIsEmptyWhenCourseIsComplete() {
        final Course course = newCourse();
        course.setAssessments(Collections.singletonList(new Assessment("Exam", 85, 100)));
        assertFalse(course.getRequiredAverageForTarget().isPresent());
    }

    @Test
    void setTopicConfidence() {
        final Course course = newCourse();
        course.setTopicConfidence("solid", ConfidenceLevel.GREEN);
        assertEquals(ConfidenceLevel.GREEN, course.getTopics().get(0).getConfidence());
        assertThrows(IllegalArgumentException.class,
                () -> course.setTopicConfidence("Generics", ConfidenceLevel.GREEN));
    }

    @Test
    void duplicateTopicIsRejected() {
        final Course course = newCourse();
        assertThrows(IllegalArgumentException.class,
                () -> course.addTopic(new Topic("SOLID", ConfidenceLevel.YELLOW)));
    }

    @Test
    void assessmentValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Assessment("", 50, 10));
        assertThrows(IllegalArgumentException.class, () -> new Assessment("Quiz", 101, 10));
        assertThrows(IllegalArgumentException.class, () -> new Assessment("Quiz", 50, 0));
    }
}
