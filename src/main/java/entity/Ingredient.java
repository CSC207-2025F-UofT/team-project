package entity;

public class Ingredient {
    private final String name;
    private final String measurement;

    public Ingredient(String name, String measurement){
        this.name = name;
        this.measurement = measurement;
    }
    public String getName(){return name;}
    public String getMeasurement(){return measurement;}
}
