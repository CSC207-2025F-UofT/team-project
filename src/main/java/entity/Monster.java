package entity;

import API.MonsterDetail;
import API.SrdMonsterDetail;

import java.util.ArrayList;
import java.util.Random;
/**
 * This Monster class
 */
public class Monster {
    public String NAME;
    private double HP;
    private Spells[] SPELL;

    /**
     * The constructor of the Monster. It randomly set the value of the HP, select name and generate spells from
     * the api.
     */
    public Monster() {
        Random random = new Random();
        HP = random.nextInt(11) + 20;

        MonsterDetail api = new SrdMonsterDetail();
        setSpells(api);
        setNAME(api);
    }

    @Override
    public String toString() {
        return NAME;
    }


    /**
     * The getter for the HP.
     */
    public double getHP() {
        return HP;
    }

    /**
     * The getter for the SPELL.
     */
    public Spells[] getSpells() {
        return SPELL;
    }

    /**
     * Decrease the HP.
     */
    public void HPDecrease(double DMG){
        HP -= DMG;
        if (HP < 0) HP = 0;
    }

    /**
     * Randomly select a name from the api.
     */
    public void setNAME(MonsterDetail api) {
        Random random = new Random();
        String[] nameList = api.generateRaces();
        int size = nameList.length;
        int index = random.nextInt(size);
        NAME = nameList[index];
    }

    /**
     * Randomly select size of the spell list for the monster and randomly select the type of spells.
     */
    public void setSpells(MonsterDetail api) {
        Random random = new Random();
        int size = random.nextInt(3) + 1;
        ArrayList<Spells> spell = api.generateSpells();
        int spellListSize = spell.size();
        SPELL = new Spells[size];
        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(spellListSize);
            SPELL[i] = spell.get(randomIndex);
        }
    }

    /**
     * Randomly select the spell
     */
    public Spells chooseSpell(){
        Random random = new Random();
        int size = SPELL.length;
        int index = random.nextInt(size);
        return SPELL[index];
    }

    /**
     * Return the DMG value of the monster.
     */
    public double attack(Spells spell) {
        return spell.getDMG();
    }

    /**
     * Return true if the HP of the monster is greater than 0, which indicate the monster is still alive.
     */
    public boolean isAlive() {
        return HP > 0;
    }
}
