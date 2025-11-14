package plan4life.use_case.block_off_time;

import java.time.LocalDateTime;

public class BlockOffTimeRequestModel {
    private final int scheduleId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String description;

    public BlockOffTimeRequestModel(int scheduleId, LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.scheduleId = scheduleId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    // getters
    public int getScheduleId() { return scheduleId; }
    public LocalDateTime getStart() { return startTime; }
    public LocalDateTime getEnd() { return endTime; }
    public String getDescription() { return description; }
}
