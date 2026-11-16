package interface_adapter.add_card;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AddCardViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private int cardId;
    private int deckId;
    private String question = "";
    private String answer = "";
    private String message = "";
    private String error = "";

    public void setSuccess(int cardId, int deckId, String question, String answer, String message){
        this.cardId = cardId;
        this.deckId = deckId;
        this.question = question;
        this.answer = answer;
        this.message = message;
        this.error = "";

        support.firePropertyChange("success cardAdded", null, null);
    }

    public void setError(String error){
        this.error = error;
        this.message = "";
        support.firePropertyChange("error cardNotAdded", null, error);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
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

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}