package use_case;

import java.util.Objects;

/** Display-only item identity. */
public final class ItemData {
    private final String collectableId;
    private final String label;

    public ItemData(String collectableId, String label) {
        this.collectableId = Objects.requireNonNull(collectableId, "collectableId");
        this.label = Objects.requireNonNull(label, "label");
    }

    public String collectableId() { return collectableId; }
    public String label() { return label; }
    @Override public String toString() { return label; }
}
