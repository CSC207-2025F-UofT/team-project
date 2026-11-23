package use_case.submit_guess;

import entity.Round;

public class SubmitGuessInputData {

  private final String guess;
  private final Round round;
//  private final int roundID; TODO: add this later? if we keep track of rounds...

  public SubmitGuessInputData(String guess, Round round) {
    this.guess = guess;
    this.round = round;
  }

  public String getGuess() {
    return guess;
  }

  public Round getRound() {
    return round;
  }
}
