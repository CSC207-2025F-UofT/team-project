package interface_adapter.explore;

import interface_adapter.ViewManagerModel;
import use_case.explore.move.MoveOutputBoundary;
import use_case.explore.move.MoveOutputData;

/**
 * Presents the result of moving between rooms.
 *
 * <p>The interactor hands it raw output data; the presenter turns that into
 * display-ready text in the ExploreViewModel and tells the view to update.</p>
 */
public class MovePresenter implements MoveOutputBoundary {
    /** Name of the combat view (Use Case 3). Change it to match the combat view's name once it exists. */
    public static final String COMBAT_VIEW_NAME = "combat";

    /** The Explore screen's data. */
    private final ExploreViewModel exploreViewModel;
    /** Used to switch between screens. */
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates the presenter.
     *
     * @param exploreViewModel the Explore screen's view model
     * @param viewManagerModel the model that controls which screen is shown
     */
    public MovePresenter(ExploreViewModel exploreViewModel, ViewManagerModel viewManagerModel) {
        this.exploreViewModel = exploreViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Step 4: show the new room.
     *
     * @param outputData the room the character entered
     */
    @Override
    public void prepareSuccessView(MoveOutputData outputData) {
        // Update the screen's data.
        ExploreState state = exploreViewModel.getState();
        fillRoom(state, outputData);
        state.setMessage("You enter the " + outputData.getRoomName() + ".");
        state.setError(false);
        // Tell ExploreView to redraw.
        exploreViewModel.notifyStateChanged();

        // Make sure the Explore screen is the one being shown.
        viewManagerModel.setState(exploreViewModel.getViewName());
        viewManagerModel.notifyViewChanged();
    }

    /**
     * Extension 2a: show why the move failed. The room itself hasn't changed,
     * so only the message is updated.
     *
     * @param errorMessage message to show
     */
    @Override
    public void prepareFailView(String errorMessage) {
        ExploreState state = exploreViewModel.getState();
        state.setMessage(errorMessage);
        state.setError(true);
        exploreViewModel.notifyStateChanged();
    }

    /**
     * Extension 4a: a monster is here, so switch to the combat screen.
     *
     * @param outputData the room the character entered
     */
    @Override
    public void prepareCombatView(MoveOutputData outputData) {
        // Update the Explore screen too, so it's correct when the player comes back after combat.
        ExploreState state = exploreViewModel.getState();
        fillRoom(state, outputData);
        state.setMessage("A " + outputData.getMonsterName() + " blocks your way! Prepare to fight.");
        state.setError(true);
        exploreViewModel.notifyStateChanged();

        // TODO (Use Case 3): put the monster into the CombatViewModel's state here,
        //  or have the combat view load it from the DAO when it opens.
        viewManagerModel.setState(COMBAT_VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    /**
     * Copies the room and inventory information from the output data into the state.
     *
     * @param state the state to fill
     * @param data  the output data from the interactor
     */
    private static void fillRoom(ExploreState state, MoveOutputData data) {
        state.setFloorNumber(data.getFloorNumber());
        state.setRoomName(data.getRoomName());
        state.setRoomDescription(data.getRoomDescription());
        state.setRoomItemNames(ExploreFormatter.names(data.getRoomItems()));
        state.setRoomItemDetails(ExploreFormatter.details(data.getRoomItems()));
        state.setExits(data.getExits());
        state.setInventoryItemNames(ExploreFormatter.names(data.getInventoryItems()));
        state.setInventoryCapacity(data.getInventoryCapacity());
        // Moving to a new room cancels any unfinished "inventory full" choice.
        state.setPendingItemName(null);
    }
}
