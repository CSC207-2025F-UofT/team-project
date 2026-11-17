package entities;

import java.util.ArrayList;

public class Candle_Game {

    // Refer to the API documentation for the meaning of these fields.
    private final String secretCode;  // holds the secret code
    private final int maxTries; // holds the max tries user can make
    private GameCondition gameCondition; // If the game was Won, Lost, still going
    private  List<Guess> guesses;

    enum GameCondition{
        WON,
        LOSS,
        QUIT,
        TIME_ENDED,
        ON_GOING,
    }


    public Candle_Game(String secretCode, int maxTries) {
        this.secretCode = secretCode;
        this.maxTries = maxTries;
        this.gameCondition = GameCondition.ON_GOING;
        this.Guesses = new ArrayList<>(); // until we assign a guess, it will be an empty list
    }

    /**
     * Returns the secret code.
     * @return the secret code .
     */
    public String getSecretCode() {
        return secretCode;
    }

    /**
     * Returns the Max attempt.
     * @return the Max attempt.
     */
    public int getMaxTries() {
        return maxTries;
    }

    /**
     * Returns how many tries were attempted
     * @return how many tries were attempted
     */
    public int getAttemptedTries() {
        return guesses.size();
    }


    /**
     * Returns the current condition of the game
     * @return the current condition of the game
     */
    public GameCondition getGameCondition() {
        return gameCondition;
    }

    /**
     * Returns the list of all guesses
     * @return the list of all guesses
     */
    public List<Guess> getGuesses() {

        return guesses;
    }

    public static String secretCode(){
        return
    }








}

