package use_case.earn_reward_coins;

public interface EarnRewardCoinsStatusOutputBoundary {
    void presentStatus(
            int balance,
            boolean eligible,
            boolean alreadyClaimed,
            String message);
}