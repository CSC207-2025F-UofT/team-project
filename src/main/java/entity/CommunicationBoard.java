package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The vocabulary available to the child, organized as actions and related things.
 * The main screen shows up to 6 actions (e.g. "play"); each action links to
 * up to 6 related things (e.g. "ball", "toy"). A caregiver edits the board.
 */
public final class CommunicationBoard {

    public static final int MAX_ACTIONS = 6;
    public static final int MAX_THINGS_PER_ACTION = 6;

    /** Each action, in the caregiver's order, mapped to its related things. */
    private final Map<Pictogram, List<Pictogram>> actions = new LinkedHashMap<>();

    /**
     * Adds an action to the end of the main board.
     */
    public void addAction(Pictogram action) {
        Objects.requireNonNull(action, "action cannot be null");
        if (!action.isAction()) {
            throw new IllegalArgumentException(
                    "only actions can go on the main board: " + action.getLabel());
        }
        if (containsAction(action)) {
            throw new IllegalArgumentException(
                    "action is already on the board: " + action.getLabel());
        }
        if (isFull()) {
            throw new IllegalStateException("the board already has " + MAX_ACTIONS + " actions");
        }
        actions.put(action, new ArrayList<>());
    }

    /**
     * Removes an action and all of its links to related things.
     */
    public void removeAction(Pictogram action) {
        Objects.requireNonNull(action, "action cannot be null");
        if (actions.remove(action) == null) {
            throw new IllegalArgumentException(
                    "action is not on the board: " + action.getLabel());
        }
    }

    /**
     * Links a thing to an action that is already on the board.
     * The same thing can be linked to several actions.
     */
    public void addThing(Pictogram action, Pictogram thing) {
        Objects.requireNonNull(thing, "thing cannot be null");
        List<Pictogram> things = thingsOf(action);
        if (!thing.isThing()) {
            throw new IllegalArgumentException(
                    "only things can be linked to an action: " + thing.getLabel());
        }
        if (things.contains(thing)) {
            throw new IllegalArgumentException(
                    thing.getLabel() + " is already linked to " + action.getLabel());
        }
        if (things.size() >= MAX_THINGS_PER_ACTION) {
            throw new IllegalStateException(
                    action.getLabel() + " already has " + MAX_THINGS_PER_ACTION + " things");
        }
        things.add(thing);
    }

    /**
     * Removes the link between a thing and an action.
     */
    public void removeThing(Pictogram action, Pictogram thing) {
        Objects.requireNonNull(thing, "thing cannot be null");
        if (!thingsOf(action).remove(thing)) {
            throw new IllegalArgumentException(
                    thing.getLabel() + " is not linked to " + action.getLabel());
        }
    }

    public boolean containsAction(Pictogram action) {
        return actions.containsKey(action);
    }

    /** True when the main board already has the maximum number of actions. */
    public boolean isFull() {
        return actions.size() >= MAX_ACTIONS;
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    /** Returns the actions in the caregiver's order (read-only copy). */
    public List<Pictogram> getActions() {
        return Collections.unmodifiableList(new ArrayList<>(actions.keySet()));
    }

    /** Returns the things linked to an action (read-only copy). */
    public List<Pictogram> getThings(Pictogram action) {
        return Collections.unmodifiableList(new ArrayList<>(thingsOf(action)));
    }

    public boolean hasThings(Pictogram action) {
        return !thingsOf(action).isEmpty();
    }

    private List<Pictogram> thingsOf(Pictogram action) {
        Objects.requireNonNull(action, "action cannot be null");
        List<Pictogram> things = actions.get(action);
        if (things == null) {
            throw new IllegalArgumentException(
                    "action is not on the board: " + action.getLabel());
        }
        return things;
    }
}