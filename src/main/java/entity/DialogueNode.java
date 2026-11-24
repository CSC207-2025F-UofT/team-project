package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * One step of a conversation: what the character says, and the options
 * the player can choose from in reply.
 */
public class DialogueNode {

    private final String id;
    private final String text;
    private final List<DialogueOption> options;

    public DialogueNode(String id, String text, List<DialogueOption> options) {
        this.id = id;
        this.text = text;
        // Copy the list so that changing the original list later
        // does not change this node.
        this.options = new ArrayList<DialogueOption>(options);
    }

    public String getId() {
        return id;
    }

    /** What the character says at this point in the conversation. */
    public String getText() {
        return text;
    }

    public List<DialogueOption> getOptions() {
        return options;
    }

    /**
     * Finds one of this node's options by its id.
     *
     * @return the option, or null if this node has no option with that id
     */
    public DialogueOption getOption(String optionId) {
        for (int i = 0; i < options.size(); i++) {
            DialogueOption option = options.get(i);
            if (option.getId().equals(optionId)) {
                return option;
            }
        }
        return null;
    }
}
