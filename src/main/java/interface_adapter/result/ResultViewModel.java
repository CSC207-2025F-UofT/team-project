package interface_adapter.result;
import interface_adapter.results.ResultsState;

import java.util.ArrayList;
import java.util.List;

public class ResultViewModel {
//    private String message = "";
//    private final List<Runnable> listeners = new ArrayList<>();
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//        notifyListeners();
//    }
//
//    public void addListener(Runnable listener) {
//        listeners.add(listener);
//    }
//
//    private void notifyListeners() {
//        for (Runnable r : listeners) r.run();
//    }

    public static final String TITLE_LABEL = "Game Results";
    public static final String BACK_BUTTON_LABEL = "Play Again";

    public ResultViewModel() {
        //super("results");
        setState(new ResultsState());
    }

    private void setState(ResultsState resultsState) {
        //
    }
}
