package plan4life.view;

import java.time.LocalDateTime;

public interface TimeSelectionListener {
    void onTimeSelected(LocalDateTime start, LocalDateTime end, int scheduleId, int columnIndex);
}