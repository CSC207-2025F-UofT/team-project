package use_case.view_history;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The result of the View History use case: one entry per saved message,
 * oldest first. Contains only plain data, no entities.
 */
public final class ViewHistoryOutputData {

    private final List<Entry> entries;

    public ViewHistoryOutputData(List<Entry> entries) {
        this.entries = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(entries, "entries cannot be null")));
    }

    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * One message in the history.
     */
    public static final class Entry {

        private final String text;
        private final LocalDateTime timestamp;

        public Entry(String text, LocalDateTime timestamp) {
            this.text = Objects.requireNonNull(text, "text cannot be null");
            this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
        }

        public String getText() {
            return text;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}