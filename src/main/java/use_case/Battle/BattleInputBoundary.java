package use_case.Battle;

/**
 * Input Boundary for actions which are related to battle.
 */
public interface BattleInputBoundary {
    /**
     * Executes the battle use case.
     *
     * @param inputData the input battle data
     */
    void execute(BattleInputData inputData);

    void switchToQuizView();
}

