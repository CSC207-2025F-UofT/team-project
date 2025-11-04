package entity;

public class Move {
    private String name;
    private int power;
    private int accuracy;
    private String effect;
    private int type_id;
    private int damage;

    public Move(String name, int power, int accuracy, String effect, int type_id, int damage) {
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        this.effect = effect;
        this.type_id = type_id;
        this.damage = damage;
    }
}
