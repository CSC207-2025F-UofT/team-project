package interface_adapter.check_compatibility;

import java.util.ArrayList;
import java.util.List;

/**
 * What the compatibility panel shows: a headline, one line per rule, and a message when there is nothing to show.
 * Every line starts with a word (Match, Conflict, Not relevant), so it is not told by colour alone.
 */
public class CompatibilityState {
    private String headline = "";
    private List<String> lines = new ArrayList<>();
    private List<Boolean> conflicts = new ArrayList<>();
    private String message = "";
    private boolean needsPreferences;
    private boolean needsLogin;

    /**
     * Returns the one-sentence summary.
     *
     * @return the headline, empty if there is nothing to show
     */
    public String getHeadline() {
        return headline;
    }

    /**
     * Sets the summary.
     *
     * @param headline the headline
     */
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    /**
     * Returns the text of each rule's outcome.
     *
     * @return the lines, in rule order
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Returns, for each line, whether it is a conflict.
     *
     * @return one flag per line, in the same order
     */
    public List<Boolean> getConflicts() {
        return conflicts;
    }

    /**
     * Sets the lines.
     *
     * @param lines the text of each rule's outcome
     * @param conflicts for each line, whether it is a conflict
     */
    public void setLines(List<String> lines, List<Boolean> conflicts) {
        this.lines = List.copyOf(lines);
        this.conflicts = List.copyOf(conflicts);
    }

    /**
     * Returns the message shown instead of results, for example when the cat was not found.
     *
     * @return the message, empty if none
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns whether the adopter must fill in their preferences first.
     *
     * @return true if no preferences are saved
     */
    public boolean needsPreferences() {
        return needsPreferences;
    }

    /**
     * Returns whether the visitor must log in first.
     *
     * @return true if nobody is logged in
     */
    public boolean needsLogin() {
        return needsLogin;
    }

    /**
     * Sets whether the visitor must log in first.
     *
     * @param needsLogin true if nobody is logged in
     */
    public void setNeedsLogin(boolean needsLogin) {
        this.needsLogin = needsLogin;
    }

    /**
     * Sets whether the adopter must fill in their preferences first.
     *
     * @param needsPreferences true if no preferences are saved
     */
    public void setNeedsPreferences(boolean needsPreferences) {
        this.needsPreferences = needsPreferences;
    }
}
