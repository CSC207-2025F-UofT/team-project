package org.accessgraph.entity;

import java.util.Objects;

/** A one-way ride between two neighbouring stations on one line, with an estimated travel time. */
public final class Connection {
    private final String fromId;
    private final String toId;
    private final String line;
    private final int minutes;

    public Connection(String fromId, String toId, String line, int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Travel time cannot be negative.");
        }
        this.fromId = Objects.requireNonNull(fromId);
        this.toId = Objects.requireNonNull(toId);
        this.line = Objects.requireNonNull(line);
        this.minutes = minutes;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getLine() {
        return line;
    }

    public int getMinutes() {
        return minutes;
    }
}
