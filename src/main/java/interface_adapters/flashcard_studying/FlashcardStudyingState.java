package interface_adapters.flashcard_studying;

public class FlashcardStudyingState {
    private String deckTitle = "";
    private String question = "";
    private String answer = "";

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDeckTitle() {
        return deckTitle;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setDeckTitle(String title) {
        this.deckTitle = title;
    }
}

