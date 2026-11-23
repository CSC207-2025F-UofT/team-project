package entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class NPCTest {

    /** Builds a small character with two nodes, used by the tests below. */
    private NPC makeNpc() {
        List<DialogueOption> noOptions = new ArrayList<DialogueOption>();
        DialogueNode greeting = new DialogueNode("greeting", "Hello!", noOptions);
        DialogueNode goodbye = new DialogueNode("goodbye", "Bye!", noOptions);

        List<DialogueNode> nodes = new ArrayList<DialogueNode>();
        nodes.add(greeting);
        nodes.add(goodbye);
        return new NPC("ta", "Stressed TA", "greeting", nodes);
    }

    @Test
    void gettersReturnWhatWasPassedIn() {
        NPC npc = makeNpc();
        assertEquals("ta", npc.getId());
        assertEquals("Stressed TA", npc.getName());
        assertEquals("greeting", npc.getStartNodeId());
    }

    @Test
    void getNodeFindsNodeById() {
        NPC npc = makeNpc();
        assertEquals("Bye!", npc.getNode("goodbye").getText());
    }

    @Test
    void getNodeReturnsNullForUnknownId() {
        NPC npc = makeNpc();
        assertNull(npc.getNode("does_not_exist"));
    }

    @Test
    void getStartNodeReturnsTheStartNode() {
        NPC npc = makeNpc();
        assertSame(npc.getNode("greeting"), npc.getStartNode());
        assertEquals("Hello!", npc.getStartNode().getText());
    }

    @Test
    void changingTheOriginalListDoesNotChangeTheNpc() {
        List<DialogueNode> nodes = new ArrayList<DialogueNode>();
        nodes.add(new DialogueNode("greeting", "Hello!", new ArrayList<DialogueOption>()));
        NPC npc = new NPC("ta", "Stressed TA", "greeting", nodes);

        nodes.clear();

        assertEquals("Hello!", npc.getStartNode().getText());
    }
}
