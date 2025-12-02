package API;

import entity.Item;
import entity.Spells;

import java.util.ArrayList;
import java.util.HashMap;

public interface MonsterDetail {
    /**
     * this method will get the urls from the api
     * @throws MonsterNotFoundException throws the exception if nothing is founded
     */
    HashMap<String,String> getAllResourcesURL() throws MonsterNotFoundException;

    /**
     * this method will get the HashMap for spells, where the keys are name of the spells and the values are the dmg
     * @throws MonsterNotFoundException throws the exception if nothing is founded
     */
    ArrayList<Spells> generateSpells() throws MonsterNotFoundException;

    /**
     * this method will get an Array for races
     * @throws MonsterNotFoundException throws the exception if nothing is founded
     */
    String[] generateRaces() throws MonsterNotFoundException;

    /**
     * this method will get an HashMap for items, where the keys are the names of the items
     * @throws MonsterNotFoundException throws the exception if nothing is found
     */
    ArrayList<Item> generateItems() throws MonsterNotFoundException;

    class MonsterNotFoundException extends RuntimeException {
        public MonsterNotFoundException() {
            super("Monster not found");
        }
    }
}
