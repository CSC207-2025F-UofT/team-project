package entity;

public class Location {

    private final String name;
    private final double latitude;
    private final double longitude;

    private Monster monster;
//    private Item item;

    public Location(String name, double latitude, double longitude, Monster monster) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
//        assert (monster == null && item != null) || (monster != null && item == null);
        this.monster = monster;
//        this.item = item;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

//    public Item getItem() {
//        return item;
//    }
//
//    public void setItem(Item item) {
//        this.item = item;
//    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}