package entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * How confident a student feels about a topic in a course.
 * The only instances are the constants declared in this class.
 */
public final class ConfidenceLevel {
    /** The student is confident with the topic. */
    public static final ConfidenceLevel GREEN = new ConfidenceLevel("GREEN");
    /** The student is not so confident with the topic. */
    public static final ConfidenceLevel YELLOW = new ConfidenceLevel("YELLOW");
    /** The topic needs more attention. */
    public static final ConfidenceLevel RED = new ConfidenceLevel("RED");

    /** Every confidence level, from most to least confident. */
    private static final List<ConfidenceLevel> ALL_LEVELS =
            Collections.unmodifiableList(Arrays.asList(GREEN, YELLOW, RED));

    private final String name;

    private ConfidenceLevel(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this confidence level, such as "GREEN".
     * @return the name of this confidence level
     */
    public String getName() {
        return name;
    }

    /**
     * Returns every confidence level, from most to least confident.
     * @return a read-only list of all the confidence levels
     */
    public static List<ConfidenceLevel> values() {
        return ALL_LEVELS;
    }

    /**
     * Returns the confidence level with the given name, ignoring case and surrounding spaces.
     * @param name the name of a confidence level, such as "yellow"
     * @return the matching confidence level
     * @throws IllegalArgumentException if no confidence level has that name
     */
    public static ConfidenceLevel fromName(String name) {
        if (name != null) {
            for (ConfidenceLevel level : ALL_LEVELS) {
                if (level.name.equalsIgnoreCase(name.trim())) {
                    return level;
                }
            }
        }
        throw new IllegalArgumentException("Unknown confidence level: " + name);
    }

    @Override
    public String toString() {
        return name;
    }
}
