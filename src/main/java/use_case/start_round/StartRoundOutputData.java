package use_case.start_round;

public class StartRoundOutputData {
    private final String imageURL;
    private final int attemptsLeft;

    public StartRoundOutputData(String imageURL, int attemptsLeft) {
        this.imageURL = imageURL;
        this.attemptsLeft = attemptsLeft;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }
}