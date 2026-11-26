package interface_adapter.Reviews;

import java.util.ArrayList;
import java.util.List;

public class ReviewsState {
    private List<String> reviews = new ArrayList<>();
    private String errorMessages = "";

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = new ArrayList<>(reviews);
    }

    public String getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }
}

