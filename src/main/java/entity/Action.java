package entity;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable configuration of one object choice; it never changes the world itself. */
public final class Action {
    private final String id;
    private final String label;
    private final Optional<String> requiredItemId;
    private final Set<String> appearingItemIds;
    private final String resultText;
    private final boolean repeatable;
    private final List<Condition> preconditions;
    private final List<Effect> effects;

    public Action(String id, String label, Optional<String> requiredItemId,
                  Set<String> appearingItemIds, String resultText, boolean repeatable,
                  List<Condition> preconditions, List<Effect> effects) {
        this.id = requireText(id, "id");
        this.label = requireText(label, "label");
        this.requiredItemId = Objects.requireNonNull(requiredItemId, "requiredItemId")
                .map(value -> requireText(value, "requiredItemId"));
        LinkedHashSet<String> appearances = new LinkedHashSet<>();
        Objects.requireNonNull(appearingItemIds, "appearingItemIds")
                .forEach(value -> appearances.add(requireText(value, "appearing item ID")));
        this.appearingItemIds = Collections.unmodifiableSet(appearances);
        this.resultText = requireText(resultText, "resultText");
        this.repeatable = repeatable;
        this.preconditions = List.copyOf(Objects.requireNonNull(preconditions, "preconditions"));
        this.effects = List.copyOf(Objects.requireNonNull(effects, "effects"));
        if (this.effects.stream().anyMatch(effect -> !(effect instanceof SetObjectStateEffect))) {
            throw new IllegalArgumentException("Only SetObjectStateEffect is supported");
        }
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
    }

    public boolean requiresItem() {
        return requiredItemId.isPresent();
    }

    public Optional<String> requiredItemId() {
        return requiredItemId;
    }

    public Set<String> appearingItemIds() {
        return appearingItemIds;
    }

    public String resultText() {
        return resultText;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public boolean isAvailable(RoomReadState facts, Set<String> itemIds) {
        Objects.requireNonNull(facts, "facts");
        Objects.requireNonNull(itemIds, "itemIds");
        if (requiredItemId.isPresent() && !itemIds.contains(requiredItemId.get())) {
            return false;
        }
        try {
            validateState(facts);
            return true;
        }
        catch (DomainException unavailable) {
            return false;
        }
    }

    ActionPlan prepare(String objectId, RoomReadState facts, Optional<Collectable> item) {
        requireText(objectId, "objectId");
        validate(facts, item);
        return new ActionPlan(objectId, this, item);
    }

    void validate(RoomReadState facts, Optional<Collectable> item) {
        Objects.requireNonNull(item, "item");
        if (requiredItemId.isPresent() && item.isEmpty()) {
            throw new DomainException(ResultCode.MISSING_ITEM, "This action requires an item you hold.");
        }
        if (item.isPresent() && !requiredItemId.equals(Optional.of(item.get().id()))) {
            throw new DomainException(ResultCode.WRONG_ITEM, "That item does not match this action.");
        }
        validateState(Objects.requireNonNull(facts, "facts"));
    }

    private void validateState(RoomReadState facts) {
        for (Condition precondition : preconditions) {
            if (!precondition.test(facts)) {
                throw new DomainException(ResultCode.UNMET_CONDITION,
                        "The conditions for this action have not been met.");
            }
        }
        effects.forEach(effect -> effect.validate(facts));
    }

    List<Effect> effects() {
        return effects;
    }

    private static String requireText(String value, String name) {
        if (Objects.requireNonNull(value, name).isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
