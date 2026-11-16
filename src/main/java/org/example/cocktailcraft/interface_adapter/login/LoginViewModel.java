package org.example.cocktailcraft.interface_adapter.login;

import org.example.cocktailcraft.interface_adapter.ViewModel;

public class LoginViewModel extends ViewModel<LoginState> {
    public LoginViewModel() {
        super(new LoginState());
    }
}
