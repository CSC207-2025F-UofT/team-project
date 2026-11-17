package interface_adapter.calculate_grade;

/**
 * The state for the Grade Calculator View Model: the outcome of the last calculation.
 * The saved assessment rows live in the CourseProgressState, since they belong to the open course.
 */
public class GradeCalculatorState {
    private String resultMessage = "";
    private String calculateError;

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    /**
     * Returns why the last calculation failed.
     * @return the error, or null if the last calculation succeeded
     */
    public String getCalculateError() {
        return calculateError;
    }

    public void setCalculateError(String calculateError) {
        this.calculateError = calculateError;
    }
}
