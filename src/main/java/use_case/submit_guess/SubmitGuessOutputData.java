package use_case.submit_guess;

import java.util.LinkedHashSet;

public class SubmitGuessOutputData {

  private final LinkedHashSet<String> previouslyGuessed;
  private final boolean correctGuess;
  private final int guessesRemaining;
  private final int zoomStep;

  public SubmitGuessOutputData(boolean correctGuess, LinkedHashSet<String> previouslyGuessed,
                               int guessesRemaining, int zoomStep) {
    this.previouslyGuessed = previouslyGuessed;
    this.correctGuess = correctGuess;
    this.guessesRemaining = guessesRemaining;
    this.zoomStep = zoomStep;
  }

  public boolean isCorrectGuess() {
    return correctGuess;
  }

  public LinkedHashSet<String> getPreviouslyGuessed() {
    return this.previouslyGuessed;
  }

  public int getGuessesRemaining() { return this.guessesRemaining; }

  public int getZoomStep() { return this.zoomStep; }
}
