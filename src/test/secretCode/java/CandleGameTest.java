

import entities.Game.CandleGame;
import entities.Game.CandleType;
import entities.Game.Guess;
import entities.Game.GameCondition;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CandleGameTest {

    // Test to check if the game is updated to won
    @Test
    public void testUpdateGameConditionWon() {
        CandleGame game = new CandleGame("12345", 5);
        Guess guess = new Guess("12345", "12345");

        game.addGuess(guess);
        game.updateGameCondition();

        assertEquals(GameCondition.WON, game.getGameCondition());
    }

    // Test to check if the game is updated to lost
    @Test
    public void testUpdateGameConditionLoss() {
        CandleGame game = new CandleGame("12345", 1);
        Guess guess = new Guess("67890", "12345");

        game.addGuess(guess);
        game.updateGameCondition();

        assertEquals(GameCondition.LOSS, game.getGameCondition());
    }

    //Test to check if the game is updated to ongoing
    @Test
    public void testUpdateGameConditionOngoing() {
        CandleGame game = new CandleGame("12345", 5);
        Guess guess = new Guess("67890", "12345");

        game.addGuess(guess);
        game.updateGameCondition();

        assertEquals(GameCondition.ON_GOING, game.getGameCondition());
    }

    //Test to check if guess is added to guesses if just one
    @Test
    public void testAddGuessOne() {
        CandleGame game = new CandleGame("12345", 1);
        Guess guess = new Guess("12345", "12345");

        game.addGuess(guess);

        assertEquals(game.getGuesses().get(0), guess);
    }

    //Test to check if guess is added to guesses if five guesses and the previous and first guess are different
    @Test
    public void testAddGuessFive() {
        CandleGame game = new CandleGame("12345", 5);
        Guess guess1 = new Guess("12345", "12345");
        Guess guess = new Guess("45678", "12345");
        game.addGuess(guess1);
        game.addGuess(guess);
        game.addGuess(guess);
        game.addGuess(guess1);
        game.addGuess(guess);
        assertEquals(game.getGuesses().get(4), guess);
    }

    //Test to check if generated feedback matches guess and all bright
    @Test
    public void testGenerateFeedbackAllBright() {
        Guess guess = new Guess("12345", "12345");

        assertEquals(guess.generateFeedback("12345"),
                List.of(CandleType.BRIGHT, CandleType.BRIGHT, CandleType.BRIGHT, CandleType.BRIGHT, CandleType.BRIGHT));
    }

    //Test to check if generated feedback matches guess and all dim
    @Test
    public void testGenerateFeedbackAllDim() {
        Guess guess = new Guess("54231", "12345");

        assertEquals(guess.generateFeedback( "12345"),
                List.of(CandleType.DIM, CandleType.DIM, CandleType.DIM, CandleType.DIM, CandleType.DIM));
    }

    //Test to check if generated feedback matches guess and all snuff
    @Test
    public void testGenerateFeedbackAllSnuff() {
        Guess guess = new Guess("67890", "12345");

        assertEquals(guess.generateFeedback("12345"),
                List.of(CandleType.SNUFF, CandleType.SNUFF, CandleType.SNUFF, CandleType.SNUFF, CandleType.SNUFF));
    }

    //Test to check if generated feedback matches guess and mix of values
    @Test
    public void testGenerateFeedbackMix() {
        Guess guess = new Guess("16723", "12345");

        assertEquals(guess.generateFeedback("12345"),
                List.of(CandleType.BRIGHT, CandleType.SNUFF, CandleType.SNUFF, CandleType.DIM, CandleType.DIM));
    }

}
