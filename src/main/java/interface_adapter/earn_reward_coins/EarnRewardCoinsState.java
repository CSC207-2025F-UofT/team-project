package interface_adapter.earn_reward_coins;

public class EarnRewardCoinsState {
    private Integer coinBalance;
    private String message = "";
    private String error = "";
    private boolean rewardClaimed;

    public Integer getCoinBalance(){
        return coinBalance;
    }

    public void setCoinBalance(Integer coinBalance){
        this.coinBalance = coinBalance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isRewardClaimed() {
        return rewardClaimed;
    }

    public void setRewardClaimed(boolean rewardClaimed) {
        this.rewardClaimed = rewardClaimed;
    }
}
