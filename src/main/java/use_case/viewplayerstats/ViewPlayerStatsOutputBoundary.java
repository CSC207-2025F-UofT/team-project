package use_case.viewplayerstats;

public interface ViewPlayerStatsOutputBoundary {
    void presentSuccess(ViewPlayerStatsOutputData outputData);

    void presentFailure(String message);
}
