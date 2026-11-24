package use_case.earn_reward_coins;

public class EarnRewardCoinsOutputData {
    private final int coinsEarned;
    private final int newBalance;

    public EarnRewardCoinsOutputData(int coinsEarned, int newBalance) {
        this.coinsEarned = coinsEarned;
        this.newBalance = newBalance;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public int getNewBalance() {
        return newBalance;
    }
}
