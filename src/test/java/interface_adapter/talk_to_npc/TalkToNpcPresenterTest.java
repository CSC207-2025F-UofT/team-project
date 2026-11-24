package interface_adapter.talk_to_npc;

import interface_adapter.ViewManagerModel;
import interface_adapter.hud.HudState;
import interface_adapter.hud.HudViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.talk_to_npc.ChooseOptionOutputData;
import use_case.talk_to_npc.StartTalkOutputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TalkToNpcPresenterTest {

    /** Remembers the name of every property change it hears. */
    private static class RecordingListener implements PropertyChangeListener {
        private final List<String> propertyNames = new ArrayList<String>();

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            propertyNames.add(evt.getPropertyName());
        }
    }

    private DialogueViewModel dialogueViewModel;
    private HudViewModel hudViewModel;
    private ViewManagerModel viewManagerModel;
    private RecordingListener dialogueListener;
    private RecordingListener hudListener;
    private TalkToNpcPresenter presenter;

    @BeforeEach
    void setUp() {
        dialogueViewModel = new DialogueViewModel();
        hudViewModel = new HudViewModel();
        viewManagerModel = new ViewManagerModel();

        dialogueListener = new RecordingListener();
        dialogueViewModel.addPropertyChangeListener(dialogueListener);
        hudListener = new RecordingListener();
        hudViewModel.addPropertyChangeListener(hudListener);

        presenter = new TalkToNpcPresenter(dialogueViewModel, hudViewModel, viewManagerModel, "game");
    }

    /** A greeting from a vendor with two responses. */
    private StartTalkOutputData makeStartData() {
        List<String> optionIds = new ArrayList<String>();
        optionIds.add("be_nice");
        optionIds.add("be_rude");
        List<String> optionTexts = new ArrayList<String>();
        optionTexts.add("Be nice");
        optionTexts.add("Be rude");
        return new StartTalkOutputData("vendor", "Vendor", "greeting", "What'll it be?",
                optionIds, optionTexts);
    }

    @Test
    void dialogueViewFillsTheStateAndSwitchesToTheDialogueScreen() {
        presenter.prepareDialogueView(makeStartData());

        DialogueState state = dialogueViewModel.getState();
        assertEquals("vendor", state.getNpcId());
        assertEquals("Vendor", state.getNpcName());
        assertEquals("greeting", state.getNodeId());
        assertEquals("What'll it be?", state.getText());
        assertEquals(2, state.getOptionIds().size());
        assertEquals("be_rude", state.getOptionIds().get(1));
        assertEquals("Be rude", state.getOptionTexts().get(1));
        assertFalse(state.isFinished());

        assertEquals(1, dialogueListener.propertyNames.size());
        assertEquals("state", dialogueListener.propertyNames.get(0));
        assertEquals("dialogue", viewManagerModel.getState());
    }

    @Test
    void startingASecondConversationClearsTheOldResult() {
        presenter.prepareDialogueView(makeStartData());
        presenter.prepareResultView(new ChooseOptionOutputData("She smiles.", 95, 100, 50, 50, false));

        presenter.prepareDialogueView(makeStartData());

        assertFalse(dialogueViewModel.getState().isFinished());
        assertEquals("", dialogueViewModel.getState().getResultMessage());
    }

    @Test
    void resultViewShowsTheMessageAndUpdatesTheStats() {
        // Pretend a new game already put Day 1 of 7 on the HUD.
        HudState before = new HudState();
        before.setDay(1);
        before.setTotalDays(7);
        before.setEnergy(100);
        before.setSanity(100);
        before.setAcademicSurvival(50);
        before.setMoney(50);
        hudViewModel.setState(before);
        presenter.prepareDialogueView(makeStartData());

        presenter.prepareResultView(new ChooseOptionOutputData("She glares.", 100, 90, 50, 50, false));

        DialogueState state = dialogueViewModel.getState();
        assertTrue(state.isFinished());
        assertEquals("She glares.", state.getResultMessage());

        HudState after = hudViewModel.getState();
        assertEquals(100, after.getEnergy());
        assertEquals(90, after.getSanity());
        assertEquals(50, after.getAcademicSurvival());
        assertEquals(50, after.getMoney());
        // The day must not be wiped out by a stats update.
        assertEquals(1, after.getDay());
        assertEquals(7, after.getTotalDays());
        assertEquals(1, hudListener.propertyNames.size());
    }

    @Test
    void resultViewStaysOnTheDialogueScreen() {
        presenter.prepareDialogueView(makeStartData());

        presenter.prepareResultView(new ChooseOptionOutputData("She glares.", 100, 90, 50, 50, false));

        assertEquals("dialogue", viewManagerModel.getState());
    }

    @Test
    void resultViewAddsTheGameOverTextWhenThePlayerLost() {
        presenter.prepareDialogueView(makeStartData());

        presenter.prepareResultView(new ChooseOptionOutputData("She glares.", 100, 0, 50, 50, true));

        String message = dialogueViewModel.getState().getResultMessage();
        assertTrue(message.startsWith("She glares."));
        assertTrue(message.contains(TalkToNpcPresenter.GAME_OVER_TEXT));
    }

    @Test
    void failViewSetsTheErrorAndFiresTheErrorProperty() {
        presenter.prepareFailView("There is nobody here to talk to.");

        assertEquals("There is nobody here to talk to.", dialogueViewModel.getState().getErrorMessage());
        assertEquals(1, dialogueListener.propertyNames.size());
        assertEquals("error", dialogueListener.propertyNames.get(0));
        // A failure must not change which screen is showing.
        assertEquals("", viewManagerModel.getState());
    }

    @Test
    void gameViewSwitchesBackToTheGameScreen() {
        presenter.prepareDialogueView(makeStartData());

        presenter.prepareGameView();

        assertEquals("game", viewManagerModel.getState());
    }
}
