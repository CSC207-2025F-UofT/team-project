package interface_adapter.submit_guess;

import java.util.LinkedHashSet;

public class SubmitGuessState {
  private final LinkedHashSet<String> previouslyGuessed = new LinkedHashSet<String>();
  private int guessesRemaining;
  private String failure;
  private int zoomStep;
  private String username;

  public void setPreviouslyGuessed(LinkedHashSet<String> guesses) {
    this.previouslyGuessed.clear();
    this.previouslyGuessed.addAll(guesses);
  }

  public void setGuessesRemaining(int remaining) {
    this.guessesRemaining = remaining;
  }

  public void setFailure(String message) {
    this.failure = message;
  }

  public void setZoomStep(int zoomStep) {
    this.zoomStep = zoomStep;
  }

  public void setUsername(String username) { this.username = username; }

  public LinkedHashSet<String> getPreviouslyGuessed() {
    return this.previouslyGuessed;
  }

  public int getGuessesRemaining() { return this.guessesRemaining; }

  public String getFailure() { return this.failure; }

  public int getZoomStep() { return this.zoomStep; }

  public String getUsername() { return this.username; }
}
