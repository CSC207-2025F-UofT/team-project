package use_case.calculate_grade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Input Data for the Calculate Grade Use Case.
 * Marks and weights are kept as the text the user typed, so that the interactor
 * can validate them and report a helpful error.
 */
public class CalculateGradeInputData {

    private final String username;
    private final String courseCode;
    private final List<AssessmentRow> rows;

    /**
     * Creates the input data.
     * @param username the user whose course is being graded
     * @param courseCode the code of the course
     * @param rows the rows entered in the grade calculator
     */
    public CalculateGradeInputData(String username, String courseCode, List<AssessmentRow> rows) {
        this.username = username;
        this.courseCode = courseCode;
        this.rows = Collections.unmodifiableList(new ArrayList<>(rows));
    }

    String getUsername() {
        return username;
    }

    String getCourseCode() {
        return courseCode;
    }

    List<AssessmentRow> getRows() {
        return rows;
    }

    /**
     * One row of the grade calculator: an assessment name, its mark and its weight, as typed.
     */
    public static class AssessmentRow {
        private final String name;
        private final String mark;
        private final String weight;

        /**
         * Creates a row.
         * @param name the name of the assessment
         * @param mark the mark earned, as a percentage
         * @param weight how much the assessment is worth, as a percentage of the course grade
         */
        public AssessmentRow(String name, String mark, String weight) {
            this.name = name;
            this.mark = mark;
            this.weight = weight;
        }

        String getName() {
            return name;
        }

        String getMark() {
            return mark;
        }

        String getWeight() {
            return weight;
        }
    }
}
