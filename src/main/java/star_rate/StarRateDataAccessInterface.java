package star_rate;
import entity.Restaurant;

public interface StarRateDataAccessInterface {
    Restaurant getRestaurantById(String id);
    String getCurrentRestaurantId();
    void setCurrentRestaurantId(String id);
    void addRestaurant(String id, Restaurant rest);
}
