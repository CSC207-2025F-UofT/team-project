package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Optional follow-up words for actions, e.g. play -> ball, toy, park.
 *
 * <p>The board itself is unchanged: it holds up to 6 pictograms of any type.
 * This class only remembers which things are linked to which action, so that
 * tapping an action can show its things. Linked things are optional follow-ups:
 * the child can still stop at the action ("I want to play.").
 */
public final class LinkedVocabulary {

    public static final int MAX_THINGS_PER_ACTION = 6;

    /** Each action maps to its things, in the order the caregiver added them. */
    private final Map<Pictogram, List<Pictogram>> links = new LinkedHashMap<>();

    /**
     * Links a thing to an action (e.g. ball under play).
     * The same thing can be linked to several actions.
     */
    public void link(Pictogram action, Pictogram thing) {
        requireAction(action);
        Objects.requireNonNull(thing, "thing cannot be null");
        if (!thing.isThing()) {
            throw new IllegalArgumentException(
                    "only things can be linked to an action: " + thing.getLabel());
        }

        List<Pictogram> things = links.computeIfAbsent(action, key -> new ArrayList<>());
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
    public void unlink(Pictogram action, Pictogram thing) {
        Objects.requireNonNull(action, "action cannot be null");
        Objects.requireNonNull(thing, "thing cannot be null");
        List<Pictogram> things = links.get(action);
        if (things == null || !things.remove(thing)) {
            throw new IllegalArgumentException(
                    thing.getLabel() + " is not linked to " + action.getLabel());
        }
        if (things.isEmpty()) {
            links.remove(action);
        }
    }

    /**
     * Removes all links of an action, e.g. when the action is removed from the board.
     * Does nothing if the action has no links.
     */
    public void removeAllLinks(Pictogram action) {
        links.remove(Objects.requireNonNull(action, "action cannot be null"));
    }

    /**
     * Removes the links of every action that is no longer on the board,
     * so the saved profile never has links for a removed action.
     */
    public void removeLinksNotOn(CommunicationBoard board) {
        Objects.requireNonNull(board, "board cannot be null");
        links.keySet().removeIf(action -> !board.contains(action));
    }

    /**
     * Returns the things linked to this action, or an empty list if there are none.
     * The returned list cannot be modified.
     */
    public List<Pictogram> getThings(Pictogram action) {
        Objects.requireNonNull(action, "action cannot be null");
        List<Pictogram> things = links.getOrDefault(action, List.of());
        return Collections.unmodifiableList(new ArrayList<>(things));
    }

    public boolean hasThings(Pictogram action) {
        return !getThings(action).isEmpty();
    }

    /**
     * Returns every action that has at least one linked thing (used when saving).
     */
    public List<Pictogram> getLinkedActions() {
        return Collections.unmodifiableList(new ArrayList<>(links.keySet()));
    }

    private static void requireAction(Pictogram action) {
        Objects.requireNonNull(action, "action cannot be null");
        if (!action.isAction()) {
            throw new IllegalArgumentException(
                    "things can only be linked to an action: " + action.getLabel());
        }
    }
}