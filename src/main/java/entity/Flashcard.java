package entity;

import java.util.ArrayList;
import java.util.List;

public class Flashcard {
    private String sourceWord;
    private String targetWord;
    private Language sourceLang;
    private Language targetLang;
    private boolean known;
    private List<String> deckIds;

    public Flashcard(String sourceWord, String targetWord, Language sourceLang, Language targetLang) {
        this.sourceWord = sourceWord;
        this.targetWord = targetWord;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.deckIds = new ArrayList<>();
        this.known = false;
    }

    public void setSourceWord(String sourceWord) {
        this.sourceWord = sourceWord;
    }

    public String getSourceWord() {
        return sourceWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public void setSourceLang(Language sourceLang) {
        this.sourceLang = sourceLang;
    }

    public void setTargetLang(Language targetLang) {
        this.targetLang = targetLang;
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
    public Language getSourceLang() {
        return sourceLang;
    }
    public Language getTargetLang() {
        return targetLang;
    }
}
