package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The set of pictograms currently available to the child.
 * A caregiver can add or remove pictograms.
 */
public final class CommunicationBoard {

    public static final int DEFAULT_CAPACITY = 6;

    private final int capacity;
    private final List<Pictogram> pictograms;

    public CommunicationBoard() {
        this(DEFAULT_CAPACITY);
    }

    public CommunicationBoard(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.pictograms = new ArrayList<>();
    }

    /**
     * Adds a pictogram to the end of the board.
     * The same pictogram cannot be added twice.
     */
    public void addPictogram(Pictogram pictogram) {
        Objects.requireNonNull(pictogram, "pictogram cannot be null");
        if (contains(pictogram)) {
            throw new IllegalArgumentException(
                    "pictogram is already on the board: " + pictogram.getLabel());
        }
        if (isFull()) {
            throw new IllegalStateException("board is full");
        }
        pictograms.add(pictogram);
    }

    /**
     * Removes a pictogram from the board.
     */
    public void removePictogram(Pictogram pictogram) {
        Objects.requireNonNull(pictogram, "pictogram cannot be null");
        if (!pictograms.remove(pictogram)) {
            throw new IllegalArgumentException(
                    "pictogram is not on the board: " + pictogram.getLabel());
        }
    }

    public boolean contains(Pictogram pictogram) {
        return pictograms.contains(pictogram);
    }

    public boolean isFull() {
        return pictograms.size() >= capacity;
    }

    public boolean isEmpty() {
        return pictograms.isEmpty();
    }

    public int size() {
        return pictograms.size();
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns a read-only copy, so callers cannot change the board directly.
     */
    public List<Pictogram> getPictograms() {
        return Collections.unmodifiableList(new ArrayList<>(pictograms));
    }
}
