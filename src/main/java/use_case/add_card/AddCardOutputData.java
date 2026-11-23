package use_case.add_card;

// values for the presenter to update into viewModel

public class AddCardOutputData {
    private final int cardId;
    private final int deckId;
    private final String question;
    private final String answer;

    public AddCardOutputData(int cardId, int deckId, String question, String answer) {
        this.cardId = cardId;
        this.deckId = deckId;
        this.question = question;
        this.answer = answer;
    }

    public int getCardId() {
        return cardId;
    }

    public int getDeckId() {
        return deckId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}