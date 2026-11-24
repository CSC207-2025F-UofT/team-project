package use_case.submit_guess;

import entity.City;
import entity.Round;

public interface SubmitGuessDataAccessInterface {
  City findCityByName(String name);
  Round getCurrentRound();
  boolean isAlreadyGuessed();

  // We won't need saveRound(). "Saving" a round will be done by just modifying the round, returned
  // by getCurrentRound(), directly. Re-saving would be redundant, but it would still work.
  void saveRound(Round round);
}
