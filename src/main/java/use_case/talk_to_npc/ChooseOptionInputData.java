package use_case.talk_to_npc;

/**
 * Input for picking a response. The three ids together say exactly which
 * response it was: which character, which point in their conversation,
 * and which option. The interactor does not remember anything between
 * calls, so the view has to send all three back.
 */
public class ChooseOptionInputData {

    private final String npcId;
    private final String nodeId;
    private final String optionId;

    public ChooseOptionInputData(String npcId, String nodeId, String optionId) {
        this.npcId = npcId;
        this.nodeId = nodeId;
        this.optionId = optionId;
    }

    public String getNpcId() {
        return npcId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getOptionId() {
        return optionId;
    }
}
