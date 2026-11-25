package entity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * An immutable prepared command. Construction and owner binding stay in the domain package;
 * the source identities prevent a plan from being used on a different runtime object.
 */
public final class ActionPlan {
    private final String objectId;
    private final Action sourceAction;
    private final Optional<Collectable> selectedItem;
    private final Interactable sourceOwner;

    ActionPlan(String objectId, Action sourceAction, Optional<Collectable> selectedItem) {
        this(objectId, sourceAction, selectedItem, null);
    }

    private ActionPlan(String objectId, Action sourceAction, Optional<Collectable> selectedItem,
                       Interactable sourceOwner) {
        this.objectId = Objects.requireNonNull(objectId, "objectId");
        this.sourceAction = Objects.requireNonNull(sourceAction, "sourceAction");
        this.selectedItem = Objects.requireNonNull(selectedItem, "selectedItem");
        this.sourceOwner = sourceOwner;
    }

    ActionPlan forOwner(Interactable owner) {
        Objects.requireNonNull(owner, "owner");
        if (!owner.id().equals(objectId) || sourceOwner != null) {
            throw new DomainException(ResultCode.INVALID_TARGET, "The action plan has an invalid owner.");
        }
        return new ActionPlan(objectId, sourceAction, selectedItem, owner);
    }

    public String objectId() {
        return objectId;
    }

    public String actionId() {
        return sourceAction.id();
    }

    public List<Effect> effects() {
        return sourceAction.effects();
    }

    public Set<String> appearingItemIds() {
        return sourceAction.appearingItemIds();
    }

    public String resultText() {
        return sourceAction.resultText();
    }

    boolean matches(Interactable owner, Action action) {
        return sourceOwner == owner && sourceAction == action && objectId.equals(owner.id());
    }

    void validate(RoomReadState facts) {
        sourceAction.validate(facts, selectedItem);
    }
}
