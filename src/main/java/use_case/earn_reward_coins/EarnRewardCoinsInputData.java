package use_case.earn_reward_coins;

public class EarnRewardCoinsInputData {
    private final String userId;

    public EarnRewardCoinsInputData(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
