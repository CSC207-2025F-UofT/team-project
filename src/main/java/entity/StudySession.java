package entity;

import java.util.List;
import java.util.Objects;

public class StudySession {
    private final int sessionId;
    private final int userId;
    private final int deckId;

    private final List<Integer> cardIds;
    private int currentCardIndex;

    public StudySession(int sessionId, int userId, int deckId, List<Integer> cardIds) {
        Objects.requireNonNull(cardIds, "Card IDs are required");


        if (cardIds.isEmpty()) {
            throw new IllegalArgumentException("Cannot study an empty deck");
        }

        this.sessionId = sessionId;
        this.userId = userId;
        this.deckId = deckId;
        this.cardIds = List.copyOf(cardIds);
        this.currentCardIndex = 0;
    }

    public int getCurrentCardIndex() {
        return currentCardIndex;
    }

    public void completeCurrentCard() {
        if (currentCardIndex >= cardIds.size()) {
            throw new IllegalStateException("All cards have already been completed");
        }
        currentCardIndex++;
    }

    public boolean isSessionComplete() {
        return currentCardIndex >= cardIds.size();
    }

    public int getProgress() {
        return (int)((currentCardIndex * 100) / cardIds.size());
    }

    // get the list of card ids we are study sessioning 
    public List<Integer> getCardIds() {
        return cardIds;
    }

    public int getUserId() {
        return userId;
    }

        public int getDeckId() {
        return deckId;
    }

    public int getSessionId() {
        return sessionId;
    }

}