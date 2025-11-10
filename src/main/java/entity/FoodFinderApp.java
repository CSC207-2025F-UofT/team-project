package entity;

import java.util.ArrayList;
import java.util.Collections;


public class FoodFinderApp {
    private List<Restaurant> restaurantList;
    private User currentUser;

    public FoodFinderApp() {
        restaurantList = new ArrayList<Restaurant>();
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public list<Restaurant> getRestaurantlist() {
        return restaurantList;
    }

    public void addToRestaurants(Restaurant restaurant) {
        restaurantList.add(restaurant);
    }

    public double getDistance(Restaurant restaurant) throws SignedOutException {
        return 0.0;
    }

    public void sortByDistance() {
        Collections.sort(restaurantList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant A, Restaurant B) {
                double distA = getDistance(A);
                double distB = getDistance(B);
                return Double.compare(distA, distB);
            }
        });
    }

    public void sortByCheapest(){
        Collections.sort(restaurantList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant A, Restaurant B) {
                double priceA = A.getPriceRange;
                double priceB = B.getPriceRange;
                return Double.compare(distA, distB);
            }
        });
    }
}

