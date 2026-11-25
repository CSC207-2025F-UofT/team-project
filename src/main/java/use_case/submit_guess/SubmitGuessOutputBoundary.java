package use_case.submit_guess;

import entity.City;
import entity.Round;

public interface SubmitGuessOutputBoundary {
  void prepareFailView(String failure);
  void prepareSuccessView(City city, Round round);
}
