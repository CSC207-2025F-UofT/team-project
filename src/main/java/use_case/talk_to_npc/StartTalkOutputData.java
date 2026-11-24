package use_case.talk_to_npc;

import java.util.List;

/**
 * What the presenter needs to show the start of a conversation: who is
 * talking, what they say, and the responses the player can pick.
 *
 * The options are sent as two lists that line up: optionIds.get(0) is
 * the id of the option whose words are optionTexts.get(0), and so on.
 * Plain Strings only, so the entities stay out of the interface adapters.
 */
public class StartTalkOutputData {

    private final String npcId;
    private final String npcName;
    private final String nodeId;
    private final String text;
    private final List<String> optionIds;
    private final List<String> optionTexts;

    public StartTalkOutputData(String npcId, String npcName, String nodeId, String text,
                               List<String> optionIds, List<String> optionTexts) {
        this.npcId = npcId;
        this.npcName = npcName;
        this.nodeId = nodeId;
        this.text = text;
        this.optionIds = optionIds;
        this.optionTexts = optionTexts;
    }

    public String getNpcId() {
        return npcId;
    }

    public String getNpcName() {
        return npcName;
    }

    public String getNodeId() {
        return nodeId;
    }

    /** What the character says. */
    public String getText() {
        return text;
    }

    public List<String> getOptionIds() {
        return optionIds;
    }

    public List<String> getOptionTexts() {
        return optionTexts;
    }
}
