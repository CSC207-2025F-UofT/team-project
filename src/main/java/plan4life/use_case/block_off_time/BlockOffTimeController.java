package plan4life.use_case.block_off_time;

import java.time.LocalDateTime;

public class BlockOffTimeController {
    private final BlockOffTimeInputBoundary interactor;

    public BlockOffTimeController(BlockOffTimeInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void blockTime(int scheduleId, LocalDateTime start, LocalDateTime end, String description) {
        BlockOffTimeRequestModel request = new BlockOffTimeRequestModel(scheduleId, start, end, description);
        interactor.execute(request);
    }
}