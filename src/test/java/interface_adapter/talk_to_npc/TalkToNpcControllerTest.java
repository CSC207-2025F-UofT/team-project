package interface_adapter.talk_to_npc;

import org.junit.jupiter.api.Test;
import use_case.talk_to_npc.ChooseOptionInputData;
import use_case.talk_to_npc.StartTalkInputData;
import use_case.talk_to_npc.TalkToNpcInputBoundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TalkToNpcControllerTest {

    /** Fake interactor: remembers what the controller sent it. */
    private static class FakeInteractor implements TalkToNpcInputBoundary {
        private StartTalkInputData startData;
        private ChooseOptionInputData chooseData;
        private boolean left = false;

        @Override
        public void start(StartTalkInputData inputData) {
            startData = inputData;
        }

        @Override
        public void choose(ChooseOptionInputData inputData) {
            chooseData = inputData;
        }

        @Override
        public void leave() {
            left = true;
        }
    }

    @Test
    void startPassesTheNpcIdToTheInteractor() {
        FakeInteractor interactor = new FakeInteractor();
        TalkToNpcController controller = new TalkToNpcController(interactor);

        controller.start("shawarma_lady");

        assertEquals("shawarma_lady", interactor.startData.getNpcId());
    }

    @Test
    void choosePassesAllThreeIdsToTheInteractor() {
        FakeInteractor interactor = new FakeInteractor();
        TalkToNpcController controller = new TalkToNpcController(interactor);

        controller.choose("shawarma_lady", "greeting", "ask_about_day");

        assertEquals("shawarma_lady", interactor.chooseData.getNpcId());
        assertEquals("greeting", interactor.chooseData.getNodeId());
        assertEquals("ask_about_day", interactor.chooseData.getOptionId());
    }

    @Test
    void leaveCallsTheInteractor() {
        FakeInteractor interactor = new FakeInteractor();
        TalkToNpcController controller = new TalkToNpcController(interactor);

        controller.leave();

        assertTrue(interactor.left);
    }
}
