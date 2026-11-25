package interface_adapter.submit_guess;

import entity.City;
import entity.Round;
import use_case.submit_guess.SubmitGuessOutputBoundary;

public class SubmitGuessPresenter implements SubmitGuessOutputBoundary {



  @Override
  public void prepareFailView(String failure) {

  }

  @Override
  public void prepareSuccessView(City city, Round round) {

  }
}
