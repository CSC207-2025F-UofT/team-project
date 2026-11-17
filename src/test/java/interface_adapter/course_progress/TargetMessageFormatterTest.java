package interface_adapter.course_progress;

import entity.LetterGrade;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TargetMessageFormatterTest {

    @Test
    void reachableTarget() {
        assertEquals("You need an average of 78.6% on the remaining 70% to reach A-.",
                TargetMessageFormatter.format("A-", false, true, 83.3, "A-", 78.571428, 70));
    }

    @Test
    void targetOutOfReach() {
        assertEquals("A+ is out of reach: you would need 140% on the remaining 50%.",
                TargetMessageFormatter.format("A+", false, false, 40, "F", 140, 50));
    }

    @Test
    void targetAlreadySecured() {
        assertEquals("You have already secured C.",
                TargetMessageFormatter.format("C", false, true, 100, "A+", -25, 20));
    }

    @Test
    void courseCompletedTargetReached() {
        assertEquals("All of your marks are in: final grade 86.5% (A). You reached your target of A.",
                TargetMessageFormatter.format("A", true, true, 86.5, "A", 0, 0));
    }

    @Test
    void courseCompletedTargetMissed() {
        assertEquals("All of your marks are in: final grade 75% (B). You did not reach your target of A.",
                TargetMessageFormatter.format("A", true, false, 75, "B", 0, 0));
    }

    @Test
    void formatNumberDropsTrailingZero() {
        assertEquals("40", TargetMessageFormatter.formatNumber(40.0));
        assertEquals("82.5", TargetMessageFormatter.formatNumber(82.5));
        assertEquals("33.3", TargetMessageFormatter.formatNumber(33.333));
    }

    @Test
    void gradeOptionsMatchLetterGrades() {
        final List<String> labels = new ArrayList<>();
        for (LetterGrade grade : LetterGrade.values()) {
            labels.add(grade.getLabel());
        }
        assertEquals(labels, CourseProgressViewModel.GRADE_OPTIONS);
    }
}
