package interface_adapter.register;

import interface_adapter.ViewModel;

public final class RegisterViewModel extends ViewModel<RegisterState> {
    public static final String VIEW_NAME = "Register";

    public RegisterViewModel() {
        super(VIEW_NAME, RegisterState.empty());
    }
}
