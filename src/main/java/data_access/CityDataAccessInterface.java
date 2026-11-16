package data_access;

import entity.AnswerCity;
import entity.City;

public interface CityDataAccessInterface {
    City findCityByName(String name);
    AnswerCity getRandomAnswerCity();
}
