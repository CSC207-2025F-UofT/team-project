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

    public list<Restaurant> getFullRestaurantlist() {
        return restaurantList;
    }

    public void addToRestaurants(Restaurant restaurant) {
        restaurantList.add(restaurant);
    }

    public double getDistance(Restaurant restaurant) {
        float userX = currentUser.getCoords()[0];
        float userY = currentUser.getCoords()[1];
        float restaurantX = restaurant.getCoords()[0];
        float restaurantY = restaurant.getCoords()[1];

        float xSquared = (float) Math.pow(restaurantX - userX, 2);
        float ySquared = (float) Math.pow(restaurantY - userY, 2);
        return Math.sqrt(xSquared + ySquared);
    }

    public List<Restaurant> getSortByClosest() {
        List<Restaurant> sortedList = new ArrayList<Restaurant>(restaurantList);
        sortedList.sort(new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant A, Restaurant B) {
                double distA = getDistance(A);
                double distB = getDistance(B);
                return Double.compare(distA, distB);
            }
        });
        return sortedList;
    }

    public List<Restaurant> getSortByCheapest(){
        List<Restaurant> sortedList = new ArrayList<Restaurant>(restaurantList);
        sortedList.sort(new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant A, Restaurant B) {
                double priceA = A.getPriceRange;
                double priceB = B.getPriceRange;
                return Double.compare(distA, distB);
            }
        });
        return sortedList;
    }
}

