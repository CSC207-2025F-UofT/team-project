package interface_adapter.challenge;

import org.junit.jupiter.api.Test;
import support.FakeChallengeState;
import use_case.challenge.ChallengeCourse;
import use_case.challenge.ClassChallengeInteractor;
import use_case.question.QuestionLoadException;
import use_case.question.QuestionTopic;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeAdapterTest {
    @Test void controllerPresentsThrownApiErrorAndLeavesConfirmationAvailableForRetry() {
        FakeChallengeState state = new FakeChallengeState();
        state.assign(new ChallengeCourse("opaque", "Ecology", QuestionTopic.ECOLOGY, null));
        ChallengeViewModel model = new ChallengeViewModel();
        ChallengePresenter presenter = new ChallengePresenter(model);
        ClassChallengeInteractor interactor = new ClassChallengeInteractor(state, (t, p, c) -> {
            throw new QuestionLoadException("No connection");
        }, presenter);
        ChallengeController controller = new ChallengeController(interactor, presenter);
        controller.open();
        assertEquals("Energy: 100", model.getState().energy);
        assertEquals("opaque", model.getState().courses.get(0).id);
        controller.select("opaque");
        assertEquals(ChallengeViewModel.Screen.CONFIRM, model.getState().screen);
        assertTrue(model.getState().instructions.contains("20 energy"));
        controller.start("opaque");
        assertEquals("No connection", model.getState().error);
        assertEquals(ChallengeViewModel.Screen.CONFIRM, model.getState().screen);
        assertEquals(100, state.energy);
        controller.open();
        assertEquals("", model.getState().error);
        controller.select("not-enrolled");
        assertFalse(model.getState().error.isEmpty());
    }
}
