package plan4life.use_case.block_off_time;

import java.time.LocalDateTime;

public class BlockOffTimeRequestModel {
    private final int scheduleId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String description;
    private final int columnIndex;

    public BlockOffTimeRequestModel(int scheduleId, LocalDateTime startTime, LocalDateTime endTime, String description, int columnIndex) {
        this.scheduleId = scheduleId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.columnIndex = columnIndex;
    }

    // getters
    public int getScheduleId() { return scheduleId; }
    public LocalDateTime getStart() { return startTime; }
    public LocalDateTime getEnd() { return endTime; }
    public String getDescription() { return description; }
    public int getColumnIndex() { return columnIndex; }
}
