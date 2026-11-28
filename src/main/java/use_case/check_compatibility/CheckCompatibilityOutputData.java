package use_case.check_compatibility;

import java.util.List;

/**
 * The result of checking one cat: one outcome per rule.
 */
public class CheckCompatibilityOutputData {
    private final String petName;
    private final List<RuleResult> results;

    /**
     * Creates the output.
     *
     * @param petName the cat's name
     * @param results one result per rule
     */
    public CheckCompatibilityOutputData(String petName, List<RuleResult> results) {
        this.petName = petName;
        this.results = List.copyOf(results);
    }

    /**
     * Returns the cat's name.
     *
     * @return the name
     */
    public String getPetName() {
        return petName;
    }

    /**
     * Returns the outcome of each rule.
     *
     * @return the results, in rule order
     */
    public List<RuleResult> getResults() {
        return results;
    }
}
