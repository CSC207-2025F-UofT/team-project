package interface_adapter.Battle;

import interface_adapter.ViewManagerModel;
import interface_adapter.move.MoveViewModel;
import use_case.Battle.Battle_OutputBoundary;
import use_case.Battle.Battle_OutputData;

/**
 * Presenter for the Battle Use Case.
 * Converts output data from the use case into a format suitable for the view.
 */
public class Battle_Presenter implements Battle_OutputBoundary {
    private final Battle_ViewModel battleViewModel;
    private final ViewManagerModel viewManagerModel;
    // private final MoveViewModel moveViewModel;

    public Battle_Presenter(Battle_ViewModel battleViewModel, ViewManagerModel viewManagerModel) {
        this.battleViewModel = battleViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Updates the battle state after each turn.
     * This is called during the battle to update HP values.
     */
    @Override
    public void updateMonsterTurnState(Battle_OutputData outputData) {
        Battle_State state = battleViewModel.getState();

        // Update HP values after each turn
        state.setUserHP(outputData.getUserHP());
        state.setMonsterHP(outputData.getMonsterHP());

        // Add a message about the turn (Optional)
        String turnMessage = String.format("Monster Completed its turn. Your HP is %.1f, Monster HP is %.1f",
                outputData.getUserHP(), outputData.getMonsterHP());
        state.setBattleMessage("\n" + turnMessage);

        // Notify the view to update
        battleViewModel.firePropertyChange();
    }

    /**
     * Updates the battle state after each turn.
     * This is called during the battle to update HP values.
     */
    @Override
    public void updateUserTurnState(Battle_OutputData outputData) {
        Battle_State state = battleViewModel.getState();

        // Update HP values after each turn
        state.setUserHP(outputData.getUserHP());
        state.setMonsterHP(outputData.getMonsterHP());

        // Add a message about the turn (Optional)
        String turnMessage = String.format("You Completed your turn. Your HP is %.1f, Monster HP is %.1f",
                outputData.getUserHP(), outputData.getMonsterHP());
        state.setBattleMessage("\n" + turnMessage);

        // Notify the view to update
        battleViewModel.firePropertyChange();
    }

    /**
     * Prepares the Win view for the Battle Use Case.
     *
     * @param outputData the output data
     */
    @Override
    public void prepareWinView(Battle_OutputData outputData) {
        // Get the current state
        Battle_State state = battleViewModel.getState();

        // Update state with final battle results
        state.setUserHP(outputData.getUserHP());
        state.setMonsterHP(outputData.getMonsterHP());

        // Set win status and message
        state.setBattleEnded(true);
        state.setUserWon(true);
        state.setBattleMessage("Victory! You defeated " + outputData.getMonster().NAME + "!");

        // Notify the view that state has changed
        battleViewModel.firePropertyChange();

        // switch to a different view after a delay
        viewManagerModel.setState("move");
        viewManagerModel.firePropertyChange();
    }

    /**
     * Prepares the Loss view for the Battle Use Case.
     *
     * @param outputData the output data
     */
    @Override
    public void prepareLossView(Battle_OutputData outputData) {
        // Get the current state
        Battle_State state = battleViewModel.getState();

        // Update state with final battle results
        state.setUser(outputData.getUser());
        state.setMonster(outputData.getMonster());

        // Set loss status and message
        state.setBattleEnded(true);
        state.setUserWon(false);
        state.setBattleMessage("Defeat! You were defeated by " + outputData.getMonster().NAME + "!");

        // Notify the view that state has changed
        battleViewModel.firePropertyChange();

        // switch to a different view after a delay
        viewManagerModel.setState("move");
        viewManagerModel.firePropertyChange();
    }

    /**
     * Prepares the Quiz view for the Battle Use Case.
     *
     */
    @Override
    public void prepareQuizView() {
        // Switch to quiz view
        viewManagerModel.setState("Quiz");
        viewManagerModel.firePropertyChange();
    }
}
