package use_case.Battle;

/**
 * Input Boundary for actions which are related to battle.
 */
public interface Battle_InputBoundary {
    /**
     * Executes the battle use case.
     *
     * @param inputData the input battle data
     */
    void execute(Battle_InputData inputData);

    void switchToQuizView();
}

