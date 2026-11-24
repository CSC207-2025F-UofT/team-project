package interface_adapter.Reviews;

import java.util.ArrayList;
import java.util.List;

public class ReviewsViewModel {
    private List<ReviewsViewModelListener> listeners = new ArrayList<>();
    public static final String VIEW_MODEL = "reviews";
    private ReviewsState state = new ReviewsState();

    public ReviewsState getState(){
        return this.state;
    }
    public void setState(ReviewsState state){
        this.state = state;
    }
    public void addListener(ReviewsViewModelListener listener){
        listeners.add(listener);
    }
    public void notifyStateChanged(){
        for(ReviewsViewModelListener listener: listeners){
            listener.stateChanged();
        }
    }
}