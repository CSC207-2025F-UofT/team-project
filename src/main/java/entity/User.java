package entity;

import java.util.ArrayList;
import java.util.Random;

public class User {
    private double HP;
    private double bonusHP = 0;
    private double DMG = 8;
    private double DEF = 0;
    private Inventory inventory;

    /**
     * The constructor for User class.
     */
    public User() {
        Random random = new Random();
        HP = random.nextInt(11) + 20;
        inventory = new Inventory(new ArrayList<>());
    }

    @Override
    public String toString() {
        return String.format("User has HP: %f, DMG: %f, DEF: %f, Inventory: %s", HP, DMG, DEF, inventory);
    }

    /**
     * The getter for HP.
     */
    public double getHP() {
        return HP;
    }

    /**
     * The getter for DMG.
     */
    public double getDMG() {
        return DMG;
    }

    /**
     * The getter for bonusHP.
     */
    public double getBonusHP(){return bonusHP;}

    /**
     * The getter for DEF.
     */
    public double getDEF() {return DEF;}

    /**
     * Decrease the bonus HP, the bonus HP is considered as shield. Return the value of the left damage to the HP.
     */
    public double bonusHPDecrease(double dmg){
        if(bonusHP > dmg){
            bonusHP -= dmg;
            return 0;
        } else {
            double returnVal = dmg - bonusHP;
            bonusHP = 0;
            return returnVal;
        }
    }

    /**
     * Decrease the HP by the damage. The actual damage depends on the DEF of the user. Some of the damage will only
     * affect the bonus HP, if the damage is high, then it will decrease the HP.
     */
    public void HPDecrease(double dmg){
        double damage = dmg * (1 - 0.08 * getDEF());
        double leftDamage = bonusHPDecrease(damage);
        HP -= leftDamage;
        if (HP < 0) HP = 0;
    }

    /**
     * Add Bonus HP
     */
    public void addBonusHP(double hp){
        bonusHP += hp;
    }

    /**
     * Add DMG, this method will be called when user equip the weapon or change the weapon.
     */
    public void addDMG(double dmg){DMG += dmg;}

    /**
     * Decrease DMG, this method will be called when user unequip the weapon or change the weapon.
     */
    public void decreaseDMG(double dmg){DMG -= dmg;}

    /**
     * Add DEF, this method will be called when user equip the armour or change the armour.
     */
    public void addDEF(double def){DEF += def;}

    /**
     * Decrease DEF, this method will be called when user unequip the armour or change the armour.
     */
    public void decreaseDEF(double def){DEF -= def;}

    /**
     * add item to user's inventory, method called when item is picked up by user
     *
     */
    public void addItem(Item item){
        inventory.addItem(item);
    }

    /**
     * remove item from inventory, method will be called when item is used by user
     *
     */
    public void removeItem(Item item){
        inventory.removeItem(item);
    }

    /**
     *
     * Find an item by its name
     */
    public Item getItemByName(String name) {
        return inventory.getItemByName(name);
    }

    /**
     *
     * @return inventory of the user
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     *
     * @return boolean whether the inventory contains any items
     */
    public boolean hasItems() {
    return !inventory.getItems().isEmpty();
    }

    /**
     * Return true if the HP of the user is greater than 0, which indicate the user is still alive.
     */
    public boolean isAlive() {return HP > 0;}

}
