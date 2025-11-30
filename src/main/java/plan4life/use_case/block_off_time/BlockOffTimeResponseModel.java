package plan4life.use_case.block_off_time;

import plan4life.entities.BlockedTime;
import plan4life.entities.Schedule;

import java.util.List;

public class BlockOffTimeResponseModel {
    private final boolean success;
    private final String message;
    private final List<BlockedTime> updatedBlockedTimes;
    private final Schedule updatedSchedule;

    public BlockOffTimeResponseModel(boolean success, String message, List<BlockedTime> updatedBlockedTimes, Schedule updatedSchedule) {
        this.success = success;
        this.message = message;
        this.updatedBlockedTimes = updatedBlockedTimes;
        this.updatedSchedule = updatedSchedule;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<BlockedTime> getUpdatedBlockedTimes() { return updatedBlockedTimes; }
    public Schedule getUpdatedSchedule() { return updatedSchedule; }
}