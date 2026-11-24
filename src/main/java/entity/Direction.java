package entity;

/**
 * The four directions a character can move in a dungeon.
 *
 * <p>An enum is used (instead of plain strings) so that only these four values
 * can ever exist, and the compiler catches typos like "NROTH".</p>
 */
public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    /**
     * Returns the direction pointing the opposite way (e.g. NORTH -> SOUTH).
     * Room.connect() uses this to create two-way doors: if B is north of A,
     * then A must be south of B.
     *
     * @return the opposite direction
     */
    public Direction opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            default:
                // The only remaining value is WEST.
                return EAST;
        }
    }

    /**
     * Returns a nicely capitalised name for showing to the player,
     * e.g. NORTH -> "North".
     *
     * @return display name
     */
    public String displayName() {
        // name() gives "NORTH"; lower-case it, then capitalise the first letter.
        String lower = name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
