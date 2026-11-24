package data_access;

import entity.AnswerCity;
import entity.City;
import entity.Round;
import use_case.start_round.StartRoundDataAccessInterface;
import use_case.submit_guess.SubmitGuessDataAccessInterface;

public class GameDataAccessObject implements SubmitGuessDataAccessInterface, StartRoundDataAccessInterface {
    private final CityDataAccessInterface cityData;
    private Round currentRound;

    public GameDataAccessObject(CityDataAccessInterface cityData) {
        this.cityData = cityData;
    }

//    @Override
//    public Round getCurrentRound() {
//        return currentRound;
//    }
//
//    @Override
//    public City findCityByName(String name) {
//        return cityData.findCityByName(name);
//    }

    @Override
    public AnswerCity getRandomAnswerCity() {
        return cityData.getRandomAnswerCity();
    }

    @Override
    public void saveRound(Round round) {
        currentRound = round;
    }


}
