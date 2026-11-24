package use_case.submit_guess;

import entity.City;
import entity.Round;

public class SubmitGuessInteractor implements SubmitGuessInputBoundary {

  private final SubmitGuessDataAccessInterface dataAccess;
  private final SubmitGuessOutputBoundary presenter;

  public SubmitGuessInteractor(SubmitGuessDataAccessInterface dataAccess,
                               SubmitGuessOutputBoundary presenter) {
    this.dataAccess = dataAccess;
    this.presenter = presenter;
  }

  @Override
  public void execute(SubmitGuessInputData inputData) {
    String guess = inputData.getGuess().trim().toLowerCase();
    City city = dataAccess.findCityByName(guess);
    Round round = inputData.getRound();

    if (guess.isEmpty()) {
      presenter.prepareFailView("Guess box empty");
    } else if (city == null) {
      presenter.prepareFailView("No city found");
      // TODO: check if City found in previous rounds
    } else if (round.getGuesses().contains(city)){
      presenter.prepareFailView("City already guessed");
    }
  }
}