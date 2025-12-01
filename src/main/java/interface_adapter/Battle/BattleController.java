package interface_adapter.Battle;

import entity.Monster;
import entity.User;
import use_case.Battle.BattleInputBoundary;
import use_case.Battle.BattleInputData;
import interface_adapter.quiz.QuizViewModel;

/**
 * Controller for the Battle Use Case.
 *
 */
public class BattleController {
    private final BattleInputBoundary battleUseCaseInteractor;
    private final QuizViewModel quizViewModel;


    public BattleController(BattleInputBoundary battleUseCaseInteractor, QuizViewModel quizViewModel) {
        this.battleUseCaseInteractor = battleUseCaseInteractor;
        this.quizViewModel = quizViewModel;
    }

    public void execute(User user, Monster monster, boolean resultOfQuiz){
        final BattleInputData battleInputData= new BattleInputData(user, monster, resultOfQuiz);
        battleUseCaseInteractor.execute(battleInputData);
    }

    public void switchToQuizView(User user, Monster monster){
        quizViewModel.getState().setUser(user);
        quizViewModel.getState().setMonster(monster);
        battleUseCaseInteractor.switchToQuizView();
    }
}
