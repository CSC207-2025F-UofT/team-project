package interface_adapter.course_progress;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Turns the "average needed on the remaining weight" numbers into a sentence for the
 * progress page. Shared by the Calculate Grade and Set Target Grade presenters.
 */
public final class TargetMessageFormatter {

    private static final double MAX_PERCENTAGE = 100;

    private TargetMessageFormatter() {
    }

    /**
     * Builds the target message.
     * @param targetGrade the label of the target grade
     * @param courseCompleted true if the whole course grade has been entered
     * @param targetReached true if the grade so far is at least the target grade
     * @param currentPercentage the grade so far, used when the course is completed
     * @param estimatedGrade the letter grade so far, used when the course is completed
     * @param requiredAverage the average needed on the remaining weight
     * @param remainingWeight how much of the course grade is still to come
     * @return a sentence describing what the student needs to reach the target
     */
    public static String format(String targetGrade, boolean courseCompleted, boolean targetReached,
                                double currentPercentage, String estimatedGrade,
                                double requiredAverage, double remainingWeight) {
        if (courseCompleted) {
            final String result;
            if (targetReached) {
                result = "You reached your target of " + targetGrade + ".";
            }
            else {
                result = "You did not reach your target of " + targetGrade + ".";
            }
            return "All of your marks are in: final grade " + formatNumber(currentPercentage)
                    + "% (" + estimatedGrade + "). " + result;
        }
        if (requiredAverage <= 0) {
            return "You have already secured " + targetGrade + ".";
        }
        final String needed = formatNumber(requiredAverage) + "%";
        final String remaining = formatNumber(remainingWeight) + "%";
        if (requiredAverage > MAX_PERCENTAGE) {
            return targetGrade + " is out of reach: you would need " + needed
                    + " on the remaining " + remaining + ".";
        }
        return "You need an average of " + needed + " on the remaining " + remaining
                + " to reach " + targetGrade + ".";
    }

    /**
     * Formats a number with at most one decimal place, such as "82.5" or "40".
     * @param number the number to format
     * @return the formatted number
     */
    public static String formatNumber(double number) {
        final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.ROOT);
        return new DecimalFormat("0.#", symbols).format(number);
    }
}
