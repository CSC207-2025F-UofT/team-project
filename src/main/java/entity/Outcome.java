package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The result of any player action: what changes in the game.
 * Every use case builds one and hands it to GameState.apply(),
 * so the game rules live in one place.
 *
 * Build one with the Builder, setting only the parts you need:
 *   Outcome o = new Outcome.Builder()
 *       .effects(new StatEffects(-5, 0, 0, 0))
 *       .moveTo("robarts")
 *       .message("You walk to Robarts.")
 *       .build();
 */
public final class Outcome {

    private final StatEffects effects;
    private final List<String> itemsGained;
    private final List<String> itemsLost;
    private final List<String> unlockedLocationIds;
    private final int timeBlocksUsed;
    private final String newLocationId;
    private final String message;

    private Outcome(Builder builder) {
        this.effects = builder.effects;
        this.itemsGained = Collections.unmodifiableList(new ArrayList<>(builder.itemsGained));
        this.itemsLost = Collections.unmodifiableList(new ArrayList<>(builder.itemsLost));
        this.unlockedLocationIds = Collections.unmodifiableList(new ArrayList<>(builder.unlockedLocationIds));
        this.timeBlocksUsed = builder.timeBlocksUsed;
        this.newLocationId = builder.newLocationId;
        this.message = builder.message;
    }

    public StatEffects getEffects() {
        return effects;
    }

    /** Item ids the player receives. */
    public List<String> getItemsGained() {
        return itemsGained;
    }

    /** Item ids the player loses. */
    public List<String> getItemsLost() {
        return itemsLost;
    }

    public List<String> getUnlockedLocationIds() {
        return unlockedLocationIds;
    }

    public int getTimeBlocksUsed() {
        return timeBlocksUsed;
    }

    /** The location the player moves to, or null if they stay where they are. */
    public String getNewLocationId() {
        return newLocationId;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {
        private StatEffects effects = StatEffects.NONE;
        private final List<String> itemsGained = new ArrayList<>();
        private final List<String> itemsLost = new ArrayList<>();
        private final List<String> unlockedLocationIds = new ArrayList<>();
        private int timeBlocksUsed = 0;
        private String newLocationId = null;
        private String message = "";

        public Builder effects(StatEffects effects) {
            this.effects = effects;
            return this;
        }

        public Builder gainItem(String itemId) {
            itemsGained.add(itemId);
            return this;
        }

        public Builder loseItem(String itemId) {
            itemsLost.add(itemId);
            return this;
        }

        public Builder unlockLocation(String locationId) {
            unlockedLocationIds.add(locationId);
            return this;
        }

        public Builder timeBlocksUsed(int timeBlocksUsed) {
            this.timeBlocksUsed = timeBlocksUsed;
            return this;
        }

        public Builder moveTo(String locationId) {
            this.newLocationId = locationId;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Outcome build() {
            return new Outcome(this);
        }
    }
}