package use_case.check_compatibility;

/**
 * The outcome of one compatibility rule: which trait was checked, whether it is a match, and why.
 */
public class RuleResult {

    /** Whether the cat's trait fits the adopter's situation. */
    public enum Status {
        /** The trait fits. */
        MATCH,
        /** The trait does not fit. */
        CONFLICT,
        /** The trait does not matter for this adopter. */
        NOT_APPLICABLE
    }

    private final String trait;
    private final Status status;
    private final String explanation;

    /**
     * Creates a result.
     *
     * @param trait a short name for what was checked, for example "Children at home"
     * @param status whether it is a match, a conflict, or not applicable
     * @param explanation one short sentence saying why
     */
    public RuleResult(String trait, Status status, String explanation) {
        this.trait = trait;
        this.status = status;
        this.explanation = explanation;
    }

    /**
     * Returns the name of the trait that was checked.
     *
     * @return the trait name
     */
    public String getTrait() {
        return trait;
    }

    /**
     * Returns the outcome.
     *
     * @return match, conflict or not applicable
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the reason for the outcome.
     *
     * @return a short explanation
     */
    public String getExplanation() {
        return explanation;
    }
}
