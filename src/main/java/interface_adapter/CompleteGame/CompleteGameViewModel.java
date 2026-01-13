package interface_adapter.CompleteGame;

public class CompleteGameViewModel {
    private String message;
    private Runnable listener;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyListener();
    }

    public void addListener(Runnable listener) {
        this.listener = listener;
    }

    private void notifyListener() {
        if (listener != null) listener.run();
    }

}
