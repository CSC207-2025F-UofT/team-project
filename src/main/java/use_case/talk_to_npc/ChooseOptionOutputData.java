package use_case.talk_to_npc;

/**
 * What the presenter needs after the player picked a response: what
 * happened (the message), the player's stats after the change, and
 * whether that response just lost them the game.
 */
public class ChooseOptionOutputData {

    private final String message;
    private final int energy;
    private final int sanity;
    private final int academicSurvival;
    private final int money;
    private final boolean gameOver;

    public ChooseOptionOutputData(String message, int energy, int sanity,
                                  int academicSurvival, int money, boolean gameOver) {
        this.message = message;
        this.energy = energy;
        this.sanity = sanity;
        this.academicSurvival = academicSurvival;
        this.money = money;
        this.gameOver = gameOver;
    }

    public String getMessage() {
        return message;
    }

    public int getEnergy() {
        return energy;
    }

    public int getSanity() {
        return sanity;
    }

    public int getAcademicSurvival() {
        return academicSurvival;
    }

    public int getMoney() {
        return money;
    }

    /** True if a stat hit 0 because of this response. */
    public boolean isGameOver() {
        return gameOver;
    }
}
