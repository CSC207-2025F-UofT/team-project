package yuhangusecase.castspell;

public class CastOutputData {
    private final String playerName;
    private final int x;
    private final int y;
    private final String spellName;
    private final String direction;

    public CastOutputData(String playerName, int x, int y,
                          String spellName, String direction) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.spellName = spellName;
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

    public String getSpellName() {
        return spellName;
    }

    public String getDirection() {
        return direction;
    }
}
