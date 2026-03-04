package interface_adapter.Battle;

import interface_adapter.ViewModel;

public class Battle_ViewModel extends ViewModel<Battle_State> {

    public Battle_ViewModel() {
        super("Battle");
        setState(new Battle_State());
    }
}
