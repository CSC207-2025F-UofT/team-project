package view.deck_view;

/**
 * Class used for swing JList in DeckView
 */
public class SwingDeck {
    private final String question;
    private final String answer;

    public SwingDeck(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
}
