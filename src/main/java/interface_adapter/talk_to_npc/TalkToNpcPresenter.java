package interface_adapter.talk_to_npc;

import interface_adapter.ViewManagerModel;
import interface_adapter.hud.HudState;
import interface_adapter.hud.HudViewModel;
import use_case.talk_to_npc.ChooseOptionOutputData;
import use_case.talk_to_npc.StartTalkOutputData;
import use_case.talk_to_npc.TalkToNpcOutputBoundary;

/**
 * Shows what the talk use case did: fills in the dialogue screen,
 * refreshes the stats panel after a response, and switches screens.
 */
public class TalkToNpcPresenter implements TalkToNpcOutputBoundary {

    /** Added to the result when a response makes the player lose. */
    public static final String GAME_OVER_TEXT =
            "That was the last straw. One of your stats has hit 0, and your semester is over.";

    private final DialogueViewModel dialogueViewModel;
    private final HudViewModel hudViewModel;
    private final ViewManagerModel viewManagerModel;
    private final String gameViewName;

    /**
     * @param gameViewName the card name of the game screen, passed in by
     *                     AppBuilder so this presenter does not depend on the view layer
     */
    public TalkToNpcPresenter(DialogueViewModel dialogueViewModel, HudViewModel hudViewModel,
                              ViewManagerModel viewManagerModel, String gameViewName) {
        this.dialogueViewModel = dialogueViewModel;
        this.hudViewModel = hudViewModel;
        this.viewManagerModel = viewManagerModel;
        this.gameViewName = gameViewName;
    }

    @Override
    public void prepareDialogueView(StartTalkOutputData outputData) {
        // Start from a fresh state so nothing is left over from the
        // last conversation.
        DialogueState state = new DialogueState();
        state.setNpcId(outputData.getNpcId());
        state.setNpcName(outputData.getNpcName());
        state.setNodeId(outputData.getNodeId());
        state.setText(outputData.getText());
        state.setOptionIds(outputData.getOptionIds());
        state.setOptionTexts(outputData.getOptionTexts());
        dialogueViewModel.setState(state);
        dialogueViewModel.firePropertyChanged();

        viewManagerModel.setState(dialogueViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareResultView(ChooseOptionOutputData outputData) {
        // The stats changed, so the stats panel has to be told. We copy
        // the current HUD state so the day stays the same and only the
        // four stats are replaced.
        HudState hudState = new HudState(hudViewModel.getState());
        hudState.setEnergy(outputData.getEnergy());
        hudState.setSanity(outputData.getSanity());
        hudState.setAcademicSurvival(outputData.getAcademicSurvival());
        hudState.setMoney(outputData.getMoney());
        hudViewModel.setState(hudState);
        hudViewModel.firePropertyChanged();

        String message = outputData.getMessage();
        if (outputData.isGameOver()) {
            message = message + "\n\n" + GAME_OVER_TEXT;
        }

        DialogueState state = dialogueViewModel.getState();
        state.setFinished(true);
        state.setResultMessage(message);
        dialogueViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        DialogueState state = dialogueViewModel.getState();
        state.setErrorMessage(errorMessage);
        dialogueViewModel.firePropertyChanged(DialogueViewModel.ERROR_PROPERTY);
    }

    @Override
    public void prepareGameView() {
        viewManagerModel.setState(gameViewName);
        viewManagerModel.firePropertyChanged();
    }
}
