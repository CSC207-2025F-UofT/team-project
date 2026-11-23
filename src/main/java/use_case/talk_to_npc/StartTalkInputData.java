package use_case.talk_to_npc;

/**
 * Input for starting a conversation: who the player wants to talk to.
 */
public class StartTalkInputData {

    private final String npcId;

    public StartTalkInputData(String npcId) {
        this.npcId = npcId;
    }

    public String getNpcId() {
        return npcId;
    }
}
