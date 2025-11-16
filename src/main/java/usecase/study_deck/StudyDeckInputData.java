package usecase.study_deck;

public class StudyDeckInputData {
    private final int userId;
    private final int deckId;

    public StudyDeckInputData(int userId, int deckId) {
        this.userId = userId;
        this.deckId = deckId;
    }

    public int getUserId() {
        return userId;
    }

    public int getDeckId() {
        return deckId;
    }
}
