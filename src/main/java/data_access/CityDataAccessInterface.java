package data_access;

import entity.AnswerCity;
import entity.City;

public interface CityDataAccessInterface {
    AnswerCity getRandomAnswerCity();
    City findCityByName(String name);
}
