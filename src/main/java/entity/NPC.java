package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A character the player can talk to, like the Shawarma Lady.
 *
 * An NPC owns its conversation: a list of dialogue nodes, and the id of
 * the node the conversation starts at. In Story 4 there is only one node
 * (the greeting), but keeping a list means Story 7 can add longer
 * conversations without changing this class much.
 */
public class NPC {

    private final String id;
    private final String name;
    private final String startNodeId;
    private final List<DialogueNode> nodes;

    public NPC(String id, String name, String startNodeId, List<DialogueNode> nodes) {
        this.id = id;
        this.name = name;
        this.startNodeId = startNodeId;
        this.nodes = new ArrayList<DialogueNode>(nodes);
    }

    public String getId() {
        return id;
    }

    /** The name shown to the player, e.g. "Shawarma Lady". */
    public String getName() {
        return name;
    }

    public String getStartNodeId() {
        return startNodeId;
    }

    /**
     * Finds one of this character's dialogue nodes by its id.
     *
     * @return the node, or null if there is no node with that id
     */
    public DialogueNode getNode(String nodeId) {
        for (int i = 0; i < nodes.size(); i++) {
            DialogueNode node = nodes.get(i);
            if (node.getId().equals(nodeId)) {
                return node;
            }
        }
        return null;
    }

    /** The node every conversation with this character begins at. */
    public DialogueNode getStartNode() {
        return getNode(startNodeId);
    }
}
