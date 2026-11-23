package entity;

/**
 * One thing the player can say back to a character.
 *
 * Every option has an Outcome, which is what happens to the player when
 * they pick it (for example Sanity +10). In Story 4 every option ends the
 * conversation. Story 7 will let an option lead to another node instead.
 */
public class DialogueOption {

    private final String id;
    private final String text;
    private final Outcome outcome;

    public DialogueOption(String id, String text, Outcome outcome) {
        this.id = id;
        this.text = text;
        this.outcome = outcome;
    }

    public String getId() {
        return id;
    }

    /** The words shown on the button, e.g. "Complain about the prices". */
    public String getText() {
        return text;
    }

    /** What happens to the player when they pick this option. */
    public Outcome getOutcome() {
        return outcome;
    }
}
