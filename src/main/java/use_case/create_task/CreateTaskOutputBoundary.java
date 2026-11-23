package use_case.create_task;

import java.time.LocalDate;

public interface CreateTaskOutputBoundary {
    void prepareSuccessView(CreateTaskOutputData outputData);
    void prepareFailView(String errorMessage);
}