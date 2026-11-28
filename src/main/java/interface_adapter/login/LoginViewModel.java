package interface_adapter.login;

import java.util.List;
import java.util.ArrayList;

public class LoginViewModel {

    public static final String VIEW_NAME = "log in";

    private final List<LoginViewModelListener> listeners = new ArrayList<>();

    private LoginState state = new LoginState();

    public LoginViewModel() {}

    public LoginState getState() {
        return state;
    }

    public void setState(LoginState state) {
        this.state = state;
    }

    public void addListener(LoginViewModelListener listener) {
        listeners.add(listener);
    }

    public void resetState() {
        state = new LoginState();
        notifyStateChanged();
    }

    public void notifyStateChanged(){
        for (LoginViewModelListener listener : listeners){
            listener.stateChanged();
        }
    }
}
