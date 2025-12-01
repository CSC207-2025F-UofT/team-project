package interface_adapter.result;
import java.util.ArrayList;
import java.util.List;

public class ResultViewModel {
    private String message = "";
    private final List<Runnable> listeners = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyListeners();
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable r : listeners) r.run();
    }
}
