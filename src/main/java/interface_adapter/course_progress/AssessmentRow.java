package interface_adapter.course_progress;

/**
 * One assessment of a course as text, ready to show: its name, mark and weight.
 * Also used for the rows the student types into the grade calculator.
 */
public class AssessmentRow {
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

    public String getName() {
        return name;
    }

    public String getMark() {
        return mark;
    }

    public String getWeight() {
        return weight;
    }
}
