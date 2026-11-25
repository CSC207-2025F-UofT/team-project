package interface_adapter.register;
import java.util.ArrayList;
import java.util.List;

public class RegisterViewModel {

    private List<RegisterViewModelListener> listeners= new ArrayList<>();

    private RegisterState state = new RegisterState();
    public static final String VIEW_NAME = "register";

    public void addListener(RegisterViewModelListener listener){
        listeners.add(listener);
    }

    public RegisterState getState(){
        return this.state;
    }
}
