package interface_adapter.view_history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * What the history screen currently shows: either a list of rows
 * or an error message (e.g. when there is no history yet).
 */
public class ViewHistoryState {

    private List<HistoryRow> rows = new ArrayList<>();
    private String errorMessage;

    public List<HistoryRow> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public void setRows(List<HistoryRow> rows) {
        this.rows = new ArrayList<>(rows);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * One line on the history screen, already formatted for display.
     */
    public static final class HistoryRow {

        private final String dayLabel;
        private final String timeLabel;
        private final String text;

        public HistoryRow(String dayLabel, String timeLabel, String text) {
            this.dayLabel = dayLabel;
            this.timeLabel = timeLabel;
            this.text = text;
        }

        public String getDayLabel() {
            return dayLabel;
        }

        public String getTimeLabel() {
            return timeLabel;
        }

        public String getText() {
            return text;
        }
    }
}