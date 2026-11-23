package yuhangusecase.castspell;

public class CastInputData {
    private final String playerName;
    private final int x;
    private final int y;
    private final int spellIndex;
    private final String direction;

    public CastInputData(String playerName, int x, int y,
                         int spellIndex, String direction) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.spellIndex = spellIndex;
        this.direction = direction;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpellIndex() {
        return spellIndex;
    }

    public String getDirection() {
        return direction;
    }
}
