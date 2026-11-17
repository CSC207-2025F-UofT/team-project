package entity.board;

import entity.card.CharacterCard;
import entity.card.SorceryCard;
import entity.card.SpellCard;

import java.util.ArrayList;

public class Tile {
    private CharacterCard characterCard;
    private ArrayList<SpellCard> spells;
    private SorceryCard sorceryCard;
    //private ArrayList<ItemCard> items; //(Not yet implemented)

    public Tile() {}

    /// Is this space is empty?
    /// @return true if there is no character AND no sorcery occupying this space
    public boolean isEmpty() {
        return characterCard == null && sorceryCard == null;
    }

    //Tile setters
    public void setTile(Tile tile) {
        this.characterCard = tile.getCharacterCard();
        this.spells = tile.getSpells();
        this.sorceryCard = tile.getSorceryCard();
    }

    public void clearTile() {
        this.characterCard = null;
        this.spells = null;
        this.sorceryCard = null;
    }

    //Variable Getters and Setters
    public CharacterCard getCharacterCard() {
        return characterCard;
    }

    public void setCharacterCard(CharacterCard characterCard) {
        this.characterCard = characterCard;
    }

    public ArrayList<SpellCard> getSpells() {
        return spells;
    }

    public void addSpell(SpellCard spellCard) {
        this.spells.add(spellCard);
    }
    public void removeSpell(SpellCard spellCard) {
        this.spells.remove(spellCard);
    }
    public void setSpells(ArrayList<SpellCard> spells) {
        this.spells = spells;
    }

    public SorceryCard getSorceryCard() {
        return sorceryCard;
    }

    public void setSorceryCard(SorceryCard sorceryCard) {
        this.sorceryCard = sorceryCard;
    }
}
