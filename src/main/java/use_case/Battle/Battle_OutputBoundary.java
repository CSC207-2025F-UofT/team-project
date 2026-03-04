package use_case.Battle;

public interface Battle_OutputBoundary {
    /**
     * Updates the Monster state during combat (called after each turn).
     */
    void updateMonsterTurnState(Battle_OutputData outputData);

    /**
     * Updates the User state during combat (called after each turn).
     */
    void updateUserTurnState(Battle_OutputData outputData);

    /**
     * Prepares the Win view for the Battle Use Case.
     * @param outputData the output data
     */
    void prepareWinView(Battle_OutputData outputData);

    /**
     * Prepares the Loss view for the Battle Use Case.
     * @param outputData the output data
     */
    void prepareLossView(Battle_OutputData outputData);

    /**
     * Prepares the Quiz view for the Battle Use Case.
     */
    void prepareQuizView();
}
