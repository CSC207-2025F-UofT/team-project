package entity;

import java.util.List;
import java.util.Objects;

/** Immutable conjunction of conditions that obey the pure predicate contract. */
public final class AllOfCondition implements Condition {
    private final List<Condition> conditions;

    public AllOfCondition(List<Condition> conditions) {
        this.conditions = List.copyOf(Objects.requireNonNull(conditions, "conditions"));
        if (this.conditions.isEmpty()) {
            throw new IllegalArgumentException("AllOfCondition needs at least one condition");
        }
    }

    @Override
    public boolean test(RoomReadState facts) {
        Objects.requireNonNull(facts, "facts");
        return conditions.stream().allMatch(condition -> condition.test(facts));
    }

}
