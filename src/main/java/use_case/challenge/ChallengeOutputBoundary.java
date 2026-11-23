package use_case.challenge;

public interface ChallengeOutputBoundary {
    void present(ChallengeOutputData output);
    void presentError(String message);
}
