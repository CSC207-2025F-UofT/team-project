package interface_adapters.flashcard_studying;

public class FlashcardStudyingState {
    private String deckTitle = "";
    private String text = "";

    public String getText() {
        return text;
    }


    public String getDeckTitle() {
        return deckTitle;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDeckTitle(String title) {
        this.deckTitle = title;
    }
}

