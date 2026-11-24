package interface_adapter.talk_to_npc;

import java.util.ArrayList;
import java.util.List;

/**
 * Everything the dialogue screen needs to draw itself.
 *
 * A conversation on screen is in one of two stages:
 *   1. not finished: the character has spoken and the player is picking
 *      a response (text + options are shown);
 *   2. finished: the player picked a response and is reading what
 *      happened (resultMessage is shown).
 *
 * Mutable on purpose, like HudState: the presenter fills it in and then
 * tells the view model to fire.
 */
public class DialogueState {

    private String npcId = "";
    private String npcName = "";
    private String nodeId = "";
    private String text = "";
    private List<String> optionIds = new ArrayList<String>();
    private List<String> optionTexts = new ArrayList<String>();
    private boolean finished = false;
    private String resultMessage = "";
    private String errorMessage = "";

    public String getNpcId() {
        return npcId;
    }

    public void setNpcId(String npcId) {
        this.npcId = npcId;
    }

    public String getNpcName() {
        return npcName;
    }

    public void setNpcName(String npcName) {
        this.npcName = npcName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /** What the character says. */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /** The ids of the responses, in the same order as getOptionTexts(). */
    public List<String> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<String> optionIds) {
        this.optionIds = optionIds;
    }

    /** The words on each response button. */
    public List<String> getOptionTexts() {
        return optionTexts;
    }

    public void setOptionTexts(List<String> optionTexts) {
        this.optionTexts = optionTexts;
    }

    /** True once the player has picked a response. */
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /** What happened after the player's response. */
    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
