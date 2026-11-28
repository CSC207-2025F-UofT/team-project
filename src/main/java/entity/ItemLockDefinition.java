package entity;

import java.util.Set;

/** Immutable story configuration. */
public final class ItemLockDefinition {
    public final String id;
    public final String description;
    public final String successText;
    public final Set<String> requiredItemIds;

    public ItemLockDefinition(String id,
                              String description,
                              String successText,
                              Set<String> requiredItemIds) {
        this.id = TemplateText.require(id, "id");
        this.description = TemplateText.require(description, "description");
        this.successText = TemplateText.require(successText, "successText");
        this.requiredItemIds = Set.copyOf(requiredItemIds);
    }
}
