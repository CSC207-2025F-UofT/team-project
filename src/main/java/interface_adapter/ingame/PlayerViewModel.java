package interface_adapter.ingame;

public class PlayerViewModel {

    private final String playerId;
    private final int handSize;
    private final boolean unoDeclared;

    public PlayerViewModel(
            String playerId,
            int handSize,
            boolean unoDeclared) {

        this.playerId = playerId;
        this.handSize = handSize;
        this.unoDeclared = unoDeclared;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getHandSize() {
        return handSize;
    }

    public boolean isUnoDeclared() {
        return unoDeclared;
    }
}
