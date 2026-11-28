package entity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable story configuration. */
public final class ActionDefinition {
    public final String id;
    public final String label;
    public final Optional<String> requiredItemId;
    public final Set<String> appearingItemIds;
    public final String resultText;
    public final boolean repeatable;
    public final List<StateClause> preconditions;
    public final List<StateClause> effects;

    public ActionDefinition(String id,
                            String label,
                            Optional<String> requiredItemId,
                            Set<String> appearingItemIds,
                            String resultText,
                            boolean repeatable,
                            List<StateClause> preconditions,
                            List<StateClause> effects) {
        this.id = TemplateText.require(id, "id");
        this.label = TemplateText.require(label, "label");
        this.requiredItemId = Objects.requireNonNull(requiredItemId, "requiredItemId");
        this.appearingItemIds = Set.copyOf(appearingItemIds);
        this.resultText = TemplateText.require(resultText, "resultText");
        this.repeatable = repeatable;
        this.preconditions = List.copyOf(preconditions);
        this.effects = List.copyOf(effects);
    }
}
