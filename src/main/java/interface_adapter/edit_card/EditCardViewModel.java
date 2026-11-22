package interface_adapter.edit_card;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EditCardViewModel {
    public static final String CARD_UPDATED = "cardUpdated";
    public static final String ERROR = "error";

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    private int deckId;
    private int cardId;
    private String question = "";
    private String answer = "";
    private String message = "";
    private String error = "";

    public void setSuccess(int deckId, int cardId,
                           String question, String answer,
                           String message) {
        this.deckId = deckId;
        this.cardId = cardId;
        this.question = question;
        this.answer = answer;
        this.message = message;
        this.error = "";

        support.firePropertyChange(CARD_UPDATED, null, cardId);
    }

    public void setError(String error) {
        this.error = error;
        this.message = "";

        support.firePropertyChange(ERROR, null, error);
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
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

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}