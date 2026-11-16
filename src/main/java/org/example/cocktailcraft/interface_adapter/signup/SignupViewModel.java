package org.example.cocktailcraft.interface_adapter.signup;

import org.example.cocktailcraft.interface_adapter.ViewModel;

public class SignupViewModel extends ViewModel<SignupState> {
    public SignupViewModel() {
        super(new SignupState());
    }
}
