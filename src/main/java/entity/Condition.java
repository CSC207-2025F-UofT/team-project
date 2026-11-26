package entity;

/**
 * A pure predicate over room-local object state.
 *
 * <p>Implementations must be immutable and deterministic for the supplied facts.
 * They must not mutate runtime state, perform I/O, or use changing external state.
 * New condition types can implement this contract without changing action orchestration;
 * story-template adapters register the supported configuration tags.
 */
public interface Condition {
    boolean test(RoomReadState facts);
}
