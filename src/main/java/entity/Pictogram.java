package entity;

import java.util.Objects;

/**
 * A pictogram available on a child's communication board.
 */
public final class Pictogram {

    private final int id;
    private final String label;
    private final String imageLocation;
    private final WordType type;

    /**
     * Creates a pictogram for a thing (noun).
     * Kept so existing code keeps working; prefer the constructor with a type.
     */
    public Pictogram(int id, String label, String imageLocation) {
        this(id, label, imageLocation, WordType.THING);
    }

    public Pictogram(int id, String label, String imageLocation, WordType type) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("label cannot be blank");
        }
        if (imageLocation == null || imageLocation.isBlank()) {
            throw new IllegalArgumentException("imageLocation cannot be blank");
        }

        this.id = id;
        this.label = label;
        this.imageLocation = imageLocation;
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public WordType getType() {
        return type;
    }

    public boolean isAction() {
        return type == WordType.ACTION;
    }

    public boolean isThing() {
        return type == WordType.THING;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Pictogram)) {
            return false;
        }
        Pictogram pictogram = (Pictogram) other;
        return id == pictogram.id;
    }

    /** Must match equals(): pictograms with the same id have the same hash code. */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return label;
    }
}
