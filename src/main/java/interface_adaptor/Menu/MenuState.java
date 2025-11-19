package interface_adaptor.Menu;

import entity.Restaurant;

public class MenuState {
    private String name = "";
    private String restaurantId;
    private float rating = 0;

    public String getName(){
        return this.name;
    }
    public String getRestaurantId(){
        return this.restaurantId;
    }
    public float getRating(){
        return this.rating;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setRestaurant(String rest){
        this.restaurantId = rest;
    }
    public void setRating(float rating){
        this.rating = rating;
    }

}
