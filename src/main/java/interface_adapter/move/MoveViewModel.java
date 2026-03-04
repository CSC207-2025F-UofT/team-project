package interface_adapter.move;

import interface_adapter.ViewModel;

public class MoveViewModel extends ViewModel<MoveState> {

    public MoveViewModel() {
        super("move");
        setState(new MoveState());
    }
}
