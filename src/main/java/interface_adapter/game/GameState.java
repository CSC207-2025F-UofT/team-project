package interface_adapter.game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private String message = "";
    private int attemptsLeft = 0;
    private int score = 0;
    private boolean roundOver = false;

    private final List<String> imageURLs = new ArrayList<>();
    private int selectedImage;

    public String getImageURL() {
        return imageURLs.isEmpty() ? "" : imageURLs.get(selectedImage);
    }

    public int getImageCount() {
        return imageURLs.size();
    }

    public int getSelectedImage() {
        return selectedImage;
    }

    public void addImageURL(String imageURL) {
        imageURLs.add(imageURL);
        selectImage(getImageCount() - 1);
    }

    public void selectImage(int index) {
        if (index >= 0 && index < imageURLs.size()) {
            selectedImage = index;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public void setAttemptsLeft(int attemptsLeft) {
        this.attemptsLeft = attemptsLeft;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isRoundOver() {
        return roundOver;
    }

    public void setRoundOver(boolean roundOver) {
        this.roundOver = roundOver;
    }
}