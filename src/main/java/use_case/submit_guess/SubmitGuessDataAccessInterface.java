package use_case.submit_guess;

import entity.City;
import entity.Round;

public interface SubmitGuessDataAccessInterface {

  City findCityByName(String name);

  Round getCurrentRound();

  boolean isAlreadyGuessed();

  void saveRound(Round round);
}
