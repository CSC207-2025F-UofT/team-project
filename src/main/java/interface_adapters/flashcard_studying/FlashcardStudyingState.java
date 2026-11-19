package interface_adapters.flashcard_studying;

public class FlashcardStudyingState {
    private String deckTitle = "";
    private String front = "";

    public String getFront() {
        return front;
    }


    public String getDeckTitle() {
        return deckTitle;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setDeckTitle(String title) {
        this.deckTitle = title;
    }
}

