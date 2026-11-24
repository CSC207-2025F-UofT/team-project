package use_case.earn_reward_coins;

public interface EarnRewardCoinsOutputBoundary {
    void prepareSuccessView(EarnRewardCoinsOutputData outputData);
    void prepareFailView(String errorMessage);
}
