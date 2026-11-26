package use_case.submit_guess;

import entity.City;
import entity.Round;

import java.util.LinkedHashSet;

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
    Round round = inputData.getRound();
    if (round.isOver()) {
      presenter.prepareFailView("Round over");
      return;
    }

    String guess = inputData.getGuess().trim().toLowerCase();
    if (guess.isEmpty()) {
      presenter.prepareFailView("Guess box empty");
      return;
    }

    City city = dataAccess.findCityByName(guess);
    if (city == null) {
      presenter.prepareFailView("No city found");
    } else if (round.getGuesses().contains(city)) {
      presenter.prepareFailView("City already guessed");
    } else {
      round.addGuess(city);

      LinkedHashSet<String> guessedCities = new LinkedHashSet<String>();
      for (City guessedCity : round.getGuesses()) {
        guessedCities.add(guessedCity.getName());
      }
      boolean win = false;
      if (city.equals(round.getTarget())) {
        round.endRound(true);
        win = true;
      } else if (round.attemptsRemaining() == 0) {
        round.endRound(false);
      }
      SubmitGuessOutputData outputData = new SubmitGuessOutputData(win, guessedCities,
        round.attemptsRemaining(), round.getZoomStep());
      presenter.prepareSuccessView(outputData);
    }
  }
}