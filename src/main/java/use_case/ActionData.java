package use_case;

import java.util.Objects;

/** An available choice, identified by IDs rather than its display label. */
public final class ActionData {
    private final String objectId;
    private final String actionId;
    private final String label;
    private final boolean requiresItem;

    public ActionData(String objectId, String actionId, String label, boolean requiresItem) {
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
