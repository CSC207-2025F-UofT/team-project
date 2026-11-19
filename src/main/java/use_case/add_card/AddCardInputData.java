package use_case.add_card;

public class AddCardInputData {
    private final int deckId;
    private final String question;
    private final String answer;

    public AddCardInputData(int deckId,String question, String answer) {
        this.deckId = deckId;
        this.question = question;
        this.answer = answer;
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