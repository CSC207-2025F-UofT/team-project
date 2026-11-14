package entity;

/**
 * A letter grade, with the lowest percentage that earns it.
 * The grades are declared from highest to lowest.
 */
public enum LetterGrade {
    A_PLUS("A+", 90),
    A("A", 85),
    A_MINUS("A-", 80),
    B_PLUS("B+", 77),
    B("B", 73),
    B_MINUS("B-", 70),
    C_PLUS("C+", 67),
    C("C", 63),
    C_MINUS("C-", 60),
    D_PLUS("D+", 57),
    D("D", 53),
    D_MINUS("D-", 50),
    F("F", 0);

    private final String label;
    private final int minPercentage;

    LetterGrade(String label, int minPercentage) {
        this.label = label;
        this.minPercentage = minPercentage;
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
}
