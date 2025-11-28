package data_access;

import entity.user.User;
import entity.weatherReport.WeatherReport;

import java.util.List;

public interface UserDataAccessInterface {

    void save(User user);

    User get(String username);

    String getCurrentUsername();

    void setCurrentUsername(String username);

    void addLocation(String location);

    List<String> getLocations();

}