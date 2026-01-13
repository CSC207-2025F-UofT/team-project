package use_case.Battle;

public interface BattleOutputBoundary {
    /**
     * Updates the Monster state during combat (called after each turn).
     */
    void updateMonsterTurnState(BattleOutputData outputData);

    /**
     * Updates the User state during combat (called after each turn).
     */
    void updateUserTurnState(BattleOutputData outputData);

    /**
     * Prepares the Win view for the Battle Use Case.
     * @param outputData the output data
     */
    void prepareWinView(BattleOutputData outputData);

    /**
     * Prepares the Loss view for the Battle Use Case.
     * @param outputData the output data
     */
    void prepareLossView(BattleOutputData outputData);

    /**
     * Prepares the Quiz view for the Battle Use Case.
     */
    void prepareQuizView();
}
