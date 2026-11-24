package data_access;

import java.util.Random;
import entity.DiceRoller;

public class RandomDiceRoller implements DiceRoller {

    private final Random random;

    public RandomDiceRoller() {
        this.random = new Random();
    }

    @Override
    public int roll(int sides) {
        if (sides < 1) {
            throw new IllegalArgumentException( "Dice must have at least one side.");
        }
        return random.nextInt(sides) + 1;
    }

    @Override
    public int roll(int numberOfDice, int sides) {
        if (numberOfDice < 1) {
            throw new IllegalArgumentException( "Must roll at least one die.");
        }
        if (sides < 1) {
            throw new IllegalArgumentException( "Dice must have at least one side.");
        }
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += roll(sides);
        }
        return total;
    }
}
