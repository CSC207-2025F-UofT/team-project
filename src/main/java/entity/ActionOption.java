package entity;

import java.util.Objects;

/** Read-only available choice; IDs are used for commands, labels for display. */
public final class ActionOption {
    private final String objectId;
    private final String actionId;
    private final String label;
    private final boolean requiresItem;

    public ActionOption(String objectId, String actionId, String label, boolean requiresItem) {
        this.objectId = Objects.requireNonNull(objectId, "objectId");
        this.actionId = Objects.requireNonNull(actionId, "actionId");
        this.label = Objects.requireNonNull(label, "label");
        this.requiresItem = requiresItem;
    }

    public String objectId() { return objectId; }
    public String actionId() { return actionId; }
    public String label() { return label; }
    public boolean requiresItem() { return requiresItem; }
}
