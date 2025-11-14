package plan4life.use_case.block_off_time;

import plan4life.entities.Schedule;
import plan4life.entities.BlockedTime;
import plan4life.data_access.ScheduleDataAccessInterface;

import java.util.List;

public class BlockOffTimeInteractor implements BlockOffTimeInputBoundary {
    private final ScheduleDataAccessInterface scheduleDAO;
    private final BlockOffTimeOutputBoundary presenter;

    public BlockOffTimeInteractor(ScheduleDataAccessInterface scheduleDAO, BlockOffTimeOutputBoundary presenter) {
        this.scheduleDAO = scheduleDAO;
        this.presenter = presenter;
    }

    @Override
    public BlockOffTimeResponseModel execute(BlockOffTimeRequestModel requestModel) {
        // Retrieve schedule using the scheduleId
        Schedule schedule = scheduleDAO.getSchedule(requestModel.getScheduleId());
        if (schedule == null) {
            return fail("Schedule not found.");
        }

        // Validate input
        if (!isValidTimeRange(requestModel)) {
            return fail("Invalid time range.");
        }
        if (schedule.overlapsWithExistingBlocks(requestModel.getStart(), requestModel.getEnd())) {
            return fail("The selected time overlaps with an existing blocked period.");
        }
        schedule.removeOverlappingActivities(requestModel.getStart(), requestModel.getEnd());

        // Create and add new BlockedTime to the schedule
        BlockedTime newBlock = new BlockedTime(
                requestModel.getStart(),
                requestModel.getEnd(),
                requestModel.getDescription()
        );
        schedule.addBlockedTime(newBlock);
        scheduleDAO.saveSchedule(schedule);

        // Successful response
        BlockOffTimeResponseModel response = new BlockOffTimeResponseModel(
                true,
                "Time successfully blocked.",
                schedule.getBlockedTimes(),
                schedule
        );

        presenter.present(response);
        return response;
    }

    private BlockOffTimeResponseModel fail(String message) {
        BlockOffTimeResponseModel response = new BlockOffTimeResponseModel(false, message, List.of(), null); // List.of() is an empty immutable list
        presenter.present(response);
        return response;
    }

    private boolean isValidTimeRange(BlockOffTimeRequestModel request) {
        return request.getEnd().isAfter(request.getStart());
    }
}