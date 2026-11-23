package data_access;

import entity.City;
import entity.Round;
import use_case.submit_guess.SubmitGuessDataAccessInterface;

public class SubmitGuessDataAccessObject implements SubmitGuessDataAccessInterface {
  private final Round round;
  private final City guess;

  public SubmitGuessDataAccessObject(Round round, City guess) {
    this.round = round;
    this.guess = guess;
  }

  @Override
  public City findCityByName(String name) {
    return null;
  }

  @Override
  public Round getCurrentRound() {
    return round;
  }

  @Override
  public boolean isAlreadyGuessed() {
    return round.getGuesses().contains(guess);
  }

  @Override
  public void saveRound(Round round) {

  }
}
