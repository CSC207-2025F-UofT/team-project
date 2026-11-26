package use_case.monitoring;

public interface MonitoringOutputBoundary {
    void prepareSuccessView(MonitoringOutputData outputData);

    void prepareFailView(String errorMessage);
}
