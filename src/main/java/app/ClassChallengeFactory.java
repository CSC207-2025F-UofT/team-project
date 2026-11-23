package app;

import data_access.question.HuggingFaceQuestionGateway;
import interface_adapter.challenge.ChallengeController;
import interface_adapter.challenge.ChallengePresenter;
import interface_adapter.challenge.ChallengeViewModel;
import javax.swing.SwingUtilities;
import use_case.challenge.ChallengeStateGateway;
import use_case.challenge.ClassChallengeInteractor;
import use_case.question.QuestionGateway;
import view.ClassChallengeView;

/** Wires one Lecture Hall page to the externally supplied shared game-state adapter. */
public final class ClassChallengeFactory {
    private ClassChallengeFactory() { }

    public static ClassChallengeView create(ChallengeStateGateway state, Runnable backToStreet) {
        return create(state, new HuggingFaceQuestionGateway(), backToStreet);
    }

    public static ClassChallengeView create(ChallengeStateGateway state, QuestionGateway questions,
                                            Runnable backToStreet) {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Create the Swing page on the event dispatch thread");
        }
        ChallengeViewModel model = new ChallengeViewModel();
        ChallengePresenter presenter = new ChallengePresenter(model);
        ClassChallengeInteractor interactor = new ClassChallengeInteractor(state, questions, presenter);
        return new ClassChallengeView(new ChallengeController(interactor, presenter), model, backToStreet);
    }
}
