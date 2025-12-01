package entity;

public class FlashcardDeck {
    private final int id;
    private String title;
    private final int userId;

    public FlashcardDeck(int id, String title, int userId) {
        this.id = id;
        this.title = title;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
