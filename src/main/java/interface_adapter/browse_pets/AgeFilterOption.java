package interface_adapter.browse_pets;

/**
 * The age choices offered in the gallery's filter panel, with the age range each one stands for.
 */
public enum AgeFilterOption {
    /** No age filter. */
    ANY("Any age", null, null),
    /** Kittens under one year old. */
    UNDER_ONE("Kitten", 0, 0),
    /** One to three years old. */
    ONE_TO_THREE("Young", 1, 3),
    /** Four to seven years old. */
    FOUR_TO_SEVEN("Adult", 4, 7),
    /** Eight years or older. */
    EIGHT_PLUS("Senior", 8, Integer.MAX_VALUE);

    private final String label;
    private final Integer minYears;
    private final Integer maxYears;

    AgeFilterOption(String label, Integer minYears, Integer maxYears) {
        this.label = label;
        this.minYears = minYears;
        this.maxYears = maxYears;
    }

    /** Returns the life stage for an age in whole years. */
    public static AgeFilterOption forAge(int years) {
        for (AgeFilterOption option : values()) {
            if (option.minYears != null && years >= option.minYears && years <= option.maxYears) {
                return option;
            }
        }
        throw new IllegalArgumentException("Age cannot be negative.");
    }

    /**
     * Returns the text shown in the age drop-down.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the youngest age in the range.
     *
     * @return the minimum age in years, or null for no age filter
     */
    public Integer getMinYears() {
        return minYears;
    }

    /**
     * Returns the oldest age in the range.
     *
     * @return the maximum age in years, or null for no age filter
     */
    public Integer getMaxYears() {
        return maxYears;
    }

    @Override
    public String toString() {
        return label;
    }
}
