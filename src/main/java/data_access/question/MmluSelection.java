package data_access.question;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import use_case.question.QuestionPurpose;
import use_case.question.QuestionTopic;

/** Remote row references only: no question text, answers, cache or offline bank. */
final class MmluSelection {
    final String subject;
    final int offset;
    final List<Integer> rows;

    private MmluSelection(String subject, QuestionPurpose purpose, int[] practice, int[] exam) {
        this.subject = subject;
        offset = purpose == QuestionPurpose.PRACTICE ? 0 : 50;
        int[] selected = purpose == QuestionPurpose.PRACTICE ? practice : exam;
        rows = Arrays.stream(selected).boxed().collect(Collectors.toUnmodifiableList());
    }

    static MmluSelection forCourse(QuestionTopic topic, QuestionPurpose purpose) {
        switch (topic) {
            case PROGRAMMING_FOUNDATION:
                return new MmluSelection("high_school_computer_science", purpose,
                        new int[]{1, 21, 29}, new int[]{59, 60, 72, 65, 83});
            case SOFTWARE_DESIGN:
                return new MmluSelection("high_school_computer_science", purpose,
                        new int[]{4, 20, 34}, new int[]{57, 63, 93, 69, 71});
            case CELL_BIOLOGY:
                return new MmluSelection("high_school_biology", purpose,
                        new int[]{5, 18, 19}, new int[]{62, 67, 97, 84, 99});
            case ECOLOGY:
                return new MmluSelection("high_school_biology", purpose,
                        new int[]{8, 21, 26}, new int[]{64, 82, 91, 79, 88});
            case WORLD_HISTORY:
                return new MmluSelection("high_school_world_history", purpose,
                        new int[]{15, 23, 49}, new int[]{53, 57, 89, 62, 68});
            case HISTORICAL_PERSPECTIVE:
                return new MmluSelection("high_school_world_history", purpose,
                        new int[]{5, 38, 41}, new int[]{59, 64, 65, 79, 97});
            default:
                throw new IllegalArgumentException("Unsupported course topic");
        }
    }
}
