// Entity for Study Decks
// Archie
package entity;

import java.util.ArrayList;

public class StudyDeck {
    public String title;
    public String description;
    public ArrayList<StudyCard> deck;
    public int id;
}

// todos:
// validate ai generated / manually made decks
// max 25 cards per deck