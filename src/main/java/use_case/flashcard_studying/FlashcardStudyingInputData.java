package use_case.flashcard_studying;

public class FlashcardStudyingInputData {

    private String buttonClicked;
    private String deckTitle;

    public FlashcardStudyingInputData(String buttonClicked, String deckTitle) {
        this.buttonClicked = buttonClicked;
        this.deckTitle = deckTitle;
    }

    public FlashcardStudyingInputData(String buttonClicked) {
        this.buttonClicked = buttonClicked;
    }

    public String getButtonClicked() {
        return buttonClicked;
    }

    public String getDeckTitle() {
        return deckTitle;
    }
}
