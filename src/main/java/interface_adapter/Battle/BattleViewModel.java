package interface_adapter.Battle;

import interface_adapter.ViewModel;

public class BattleViewModel extends ViewModel<BattleState> {

    public BattleViewModel() {
        super("Battle");
        setState(new BattleState());
    }
}
