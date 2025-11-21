package interface_adapter.view_profile_reviews;

import entity.Review;

import java.util.ArrayList;
import java.util.List;

public class ProfileReviewsViewModel {

    private String username;
    private final List<Review> reviews = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Review> getReviews() {
        return new ArrayList<>(reviews);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews.clear();
        if (reviews != null) {
            this.reviews.addAll(reviews);
        }
    }
}
