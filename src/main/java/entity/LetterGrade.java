package entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A letter grade, with the lowest percentage that earns it.
 * The only instances are the constants declared in this class.
 */
public final class LetterGrade {
    public static final LetterGrade A_PLUS = new LetterGrade("A+", 90);
    public static final LetterGrade A = new LetterGrade("A", 85);
    public static final LetterGrade A_MINUS = new LetterGrade("A-", 80);
    public static final LetterGrade B_PLUS = new LetterGrade("B+", 77);
    public static final LetterGrade B = new LetterGrade("B", 73);
    public static final LetterGrade B_MINUS = new LetterGrade("B-", 70);
    public static final LetterGrade C_PLUS = new LetterGrade("C+", 67);
    public static final LetterGrade C = new LetterGrade("C", 63);
    public static final LetterGrade C_MINUS = new LetterGrade("C-", 60);
    public static final LetterGrade D_PLUS = new LetterGrade("D+", 57);
    public static final LetterGrade D = new LetterGrade("D", 53);
    public static final LetterGrade D_MINUS = new LetterGrade("D-", 50);
    public static final LetterGrade F = new LetterGrade("F", 0);

    /** Every letter grade, from highest to lowest. */
    private static final List<LetterGrade> ALL_GRADES = Collections.unmodifiableList(Arrays.asList(
            A_PLUS, A, A_MINUS, B_PLUS, B, B_MINUS, C_PLUS, C, C_MINUS, D_PLUS, D, D_MINUS, F));

    private final String label;
    private final int minPercentage;

    private LetterGrade(String label, int minPercentage) {
        this.label = label;
        this.minPercentage = minPercentage;
    }

    /**
     * Returns every letter grade, from highest to lowest.
     * @return a read-only list of all the letter grades
     */
    public static List<LetterGrade> values() {
        return ALL_GRADES;
    }

    /**
     * Returns the text shown to the user for this grade, such as "A-".
     * @return the label of this grade
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the lowest percentage that earns this grade.
     * @return the minimum percentage of this grade
     */
    public int getMinPercentage() {
        return minPercentage;
    }

    /**
     * Returns the letter grade earned by the given percentage.
     * @param percentage a percentage from 0 to 100
     * @return the highest letter grade whose minimum the percentage reaches
     * @throws IllegalArgumentException if the percentage is not between 0 and 100
     */
    public static LetterGrade fromPercentage(double percentage) {
        if (Double.isNaN(percentage) || percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        for (LetterGrade grade : values()) {
            if (percentage >= grade.minPercentage) {
                return grade;
            }
        }
        return F;
    }

    /**
     * Returns the letter grade with the given label, ignoring case and surrounding spaces.
     * @param label the label of a grade, such as "B+"
     * @return the matching letter grade
     * @throws IllegalArgumentException if no grade has that label
     */
    public static LetterGrade fromLabel(String label) {
        if (label != null) {
            for (LetterGrade grade : values()) {
                if (grade.label.equalsIgnoreCase(label.trim())) {
                    return grade;
                }
            }
        }
        throw new IllegalArgumentException("Unknown letter grade: " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}
