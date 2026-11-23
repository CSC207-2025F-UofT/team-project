package use_case.talk_to_npc;

import entity.NPC;

/**
 * Where the talk use case gets its characters and their conversations.
 *
 * The use case layer owns this interface and data_access implements it,
 * so the interactor does not care whether the conversations come from a
 * JSON file or from a fake made up in a test.
 */
public interface DialogueDataAccessInterface {

    /**
     * Looks up a character by id.
     *
     * @return the character, or null if there is no character with that id
     */
    NPC getNpc(String npcId);
}
