package use_case.edit_card;

public class EditCardInputData {
    private final int deckId;
    private final int cardId;
    private final String question;
    private final String answer;

    public EditCardInputData(int deckId, int cardId, String question, String answer) {
        this.deckId = deckId;
        this.cardId = cardId;
        this.question = question;
        this.answer = answer;
    }

    public int getDeckId() {
        return deckId;
    }

    public int getCardId() {
        return cardId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
