package entity;

import java.util.ArrayList;
import java.util.List;

public class Flashcard {
    private String sourceWord;
    private String targetWord;
    private String sourceLang;
    private String targetLang;
    private boolean known;
    private List<String> deckIds;

    public Flashcard(String sourceWord, String targetWord, String sourceLang, String targetLang) {
        this.sourceWord = sourceWord;
        this.targetWord = targetWord;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.deckIds = new ArrayList<>();
        this.known = false;
    }

    public String getSourceWord() {
        return sourceWord;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setKnown() {
        this.known = true;
    }

    public boolean isKnown() {
        return this.known;
    }

    public void addDeck(String deckId) {
        if (!deckIds.contains(deckId)) {
            deckIds.add(deckId);
        }
    }

    public void removeDeck(String deckId) {
        deckIds.remove(deckId);
    }

    public List<String> getDeckIds() {
        return deckIds;
    }
    public String getSourceLang() {
        return sourceLang;
    }
    public String getTargetLang() {
        return targetLang;
    }
}
