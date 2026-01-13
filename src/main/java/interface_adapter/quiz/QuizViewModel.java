package interface_adapter.quiz;

import interface_adapter.ViewModel;

public class QuizViewModel extends ViewModel<QuizState> {

    public QuizViewModel() {
        super("Quiz");
        setState(new QuizState());
    }
}

