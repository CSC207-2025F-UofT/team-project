package use_case.submit_guess;

public class SubmitGuessOutputData {

  private final boolean correctGuess;

  public SubmitGuessOutputData(boolean correctGuess) {
    this.correctGuess = correctGuess;
  }

  public boolean isCorrectGuess() {
    return correctGuess;
  }
}
