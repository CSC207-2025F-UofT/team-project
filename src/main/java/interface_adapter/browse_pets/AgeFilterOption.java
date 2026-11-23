package interface_adapter.browse_pets;

/**
 * The age choices offered in the gallery's filter panel, with the age range each one stands for.
 */
public enum AgeFilterOption {
    /** No age filter. */
    ANY("Any age", null, null),
    /** Kittens under one year old. */
    UNDER_ONE("Under 1 year", 0, 0),
    /** One to three years old. */
    ONE_TO_THREE("1 to 3 years", 1, 3),
    /** Four to seven years old. */
    FOUR_TO_SEVEN("4 to 7 years", 4, 7),
    /** Eight years or older. */
    EIGHT_PLUS("8 years or older", 8, 40);

    private final String label;
    private final Integer minYears;
    private final Integer maxYears;

    AgeFilterOption(String label, Integer minYears, Integer maxYears) {
        this.label = label;
        this.minYears = minYears;
        this.maxYears = maxYears;
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
