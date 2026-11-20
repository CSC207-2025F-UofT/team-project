package use_case.submit_guess;

import entity.City;

/** City dataset, as seen by Submit Guess. */
public interface SubmitGuessDataAccessInterface {
    /**
     * Looks up a guessable city, ignoring capitalization and extra spaces. Aliases like "NYC" work.
     * @return the city, or null if the name isn't a known city
     */
    City findCityByName(String name);
}
