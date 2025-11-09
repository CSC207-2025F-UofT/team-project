package entity;

public class Flashcard {
    private String sourceWord;
    private String targetWord;
    private boolean known;
    private int deckId;

    public Flashcard(String sourceWord, String targetWord, int deckId) {
        this.sourceWord = sourceWord;
        this.targetWord = targetWord;
        this.deckId = deckId;
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

    public int getDeckId() {
        return deckId;
    }
}
