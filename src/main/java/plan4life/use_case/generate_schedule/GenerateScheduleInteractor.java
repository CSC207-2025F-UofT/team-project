package plan4life.use_case.generate_schedule;

import plan4life.entities.Schedule;

public class GenerateScheduleInteractor implements GenerateScheduleInputBoundary {
    private final GenerateScheduleOutputBoundary presenter;

    public GenerateScheduleInteractor(GenerateScheduleOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateScheduleRequestModel requestModel) {
        // Core logic: generate schedule using mock or RAG logic
        Schedule schedule = new Schedule();
        schedule.populateRandomly();

        GenerateScheduleResponseModel response = new GenerateScheduleResponseModel(schedule);
        presenter.present(response);
    }
}

