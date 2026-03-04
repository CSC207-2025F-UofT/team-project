package interface_adapter.quiz;

import interface_adapter.ViewModel;

public class Quiz_ViewModel extends ViewModel<Quiz_State> {

    public Quiz_ViewModel() {
        super("Quiz");
        setState(new Quiz_State());
    }
}

