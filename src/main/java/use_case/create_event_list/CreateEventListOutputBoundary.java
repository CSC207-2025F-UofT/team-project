package use_case.create_event_list;
public interface CreateEventListOutputBoundary {
    void prepareSuccessView(CreateEventListOutputData outputData);
    void prepareFailView(String errorMessage);
}