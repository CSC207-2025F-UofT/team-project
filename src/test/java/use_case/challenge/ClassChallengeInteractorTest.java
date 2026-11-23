package use_case.challenge;

import entity.ChallengeResult;
import entity.Question;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.FakeChallengeState;
import use_case.question.QuestionGateway;
import use_case.question.QuestionLoadException;
import use_case.question.QuestionPurpose;
import use_case.question.QuestionTopic;
import static org.junit.jupiter.api.Assertions.*;
import static use_case.challenge.ChallengeInputData.Action;
import static use_case.challenge.ChallengeOutputData.Stage;

class ClassChallengeInteractorTest {
    private FakeChallengeState state;
    private Recorder recorder;
    private ClassChallengeInteractor interactor;
    private int loads;

    private static final class Recorder implements ChallengeOutputBoundary {
        private ChallengeOutputData latest;
        @Override public void present(ChallengeOutputData output) { latest = output; }
        @Override public void presentError(String message) { fail(message); }
    }

    @BeforeEach void setUp() {
        state = new FakeChallengeState();
        state.assign(new ChallengeCourse("opaque-1", "Programming Foundation",
                QuestionTopic.PROGRAMMING_FOUNDATION, null));
        recorder = new Recorder();
        interactor = create((topic, purpose, count) -> {
            loads++;
            assertEquals(QuestionPurpose.PRACTICE, purpose);
            assertEquals(3, count);
            return testQuestions();
        });
    }

    private ClassChallengeInteractor create(QuestionGateway gateway) {
        return new ClassChallengeInteractor(state, gateway, recorder);
    }

    private List<Question> testQuestions() {
        return List.of(new Question("1", "One?", List.of("A", "B"), 0, null),
                new Question("2", "Two?", List.of("C", "D"), 1, null),
                new Question("3", "Three?", List.of("E", "F"), 0, null));
    }

    private void command(Action action) throws Exception {
        ChallengeOutputData current = recorder.latest;
        interactor.execute(new ChallengeInputData(action, "opaque-1",
                current == null ? "" : current.attemptId,
                current == null ? 0 : current.questionIndex, -1, false));
    }

    private void start() throws Exception { command(Action.SELECT); command(Action.START); }

    private void answer(int option) throws Exception {
        interactor.execute(new ChallengeInputData(Action.ANSWER, "", recorder.latest.attemptId,
                recorder.latest.questionIndex, option, false));
    }

    private void finish() throws Exception {
        for (int answer : new int[]{0, 1, 1}) { answer(answer); command(Action.NEXT); }
    }

    @Test void completesWithImmediateFeedbackAndAwardsOnlyCorrectAnswers() throws Exception {
        command(Action.OPEN);
        assertEquals(1, recorder.latest.overview.getCourses().size());
        command(Action.SELECT);
        assertEquals(Stage.CONFIRM, recorder.latest.stage);
        assertEquals(100, state.energy);
        assertEquals(0, loads);
        command(Action.START);
        assertEquals(80, state.energy);
        assertEquals("", recorder.latest.feedback);
        answer(0);
        assertTrue(recorder.latest.feedback.startsWith("Correct!"));
        assertEquals(0, state.completions);
        command(Action.NEXT);
        answer(0);
        assertTrue(recorder.latest.feedback.startsWith("Incorrect."));
        assertTrue(recorder.latest.feedback.contains("D"));
        command(Action.NEXT);
        answer(0);
        command(Action.NEXT);
        assertEquals(Stage.RESULT, recorder.latest.stage);
        assertEquals(2, recorder.latest.result.getPreparationPoints());
        assertEquals(2, state.awardedPoints);
        assertNull(state.activeAttempt);
    }

    @Test void duplicateStartAnswerNextAndCompletionDoNotChargeOrRewardTwice() throws Exception {
        start();
        command(Action.START);
        assertEquals(1, loads);
        assertEquals(1, state.starts);
        String attempt = recorder.latest.attemptId;
        answer(0);
        answer(1);
        assertTrue(recorder.latest.feedback.startsWith("Correct!"));
        command(Action.NEXT);
        interactor.execute(new ChallengeInputData(Action.NEXT, "", attempt, 0, -1, false));
        interactor.execute(new ChallengeInputData(Action.ANSWER, "", attempt, 0, 0, false));
        assertEquals(Stage.QUESTION, recorder.latest.stage);
        assertEquals(1, recorder.latest.questionIndex);
        answer(1); command(Action.NEXT); answer(0); command(Action.NEXT);
        command(Action.NEXT); command(Action.START); command(Action.SELECT);
        assertEquals(1, state.completions);
        assertEquals(3, state.awardedPoints);
        assertEquals(80, state.energy);
    }

    @Test void missingAnswerAndUnconfirmedLeaveDoNotAdvanceOrReleaseLock() throws Exception {
        start();
        assertThrows(ChallengeStateException.class, () -> answer(-1));
        command(Action.NEXT);
        assertEquals(Stage.QUESTION, recorder.latest.stage);
        assertEquals(0, recorder.latest.questionIndex);
        assertThrows(ChallengeStateException.class, () -> command(Action.LEAVE));
        assertNotNull(state.activeAttempt);
    }

    @Test void cancelBeforeStartIsFreeButLeavingAnAttemptKeepsCostAndAllowsRetry() throws Exception {
        command(Action.SELECT); command(Action.LEAVE);
        assertEquals(100, state.energy);
        assertEquals(0, loads);
        command(Action.OPEN); start(); answer(0);
        String oldAttempt = recorder.latest.attemptId;
        interactor.execute(new ChallengeInputData(Action.LEAVE, "", oldAttempt, 0, -1, true));
        assertEquals(80, state.energy);
        assertEquals(0, state.completions);
        assertEquals(0, state.awardedPoints);
        command(Action.OPEN); start();
        assertEquals(60, state.energy);
        assertNotEquals(oldAttempt, recorder.latest.attemptId);
        interactor.execute(new ChallengeInputData(Action.ANSWER, "", oldAttempt, 0, 0, false));
        assertEquals(Stage.QUESTION, recorder.latest.stage);
    }

    @Test void completedResultCanBeViewedAtZeroEnergyWithoutLoadingQuestions() throws Exception {
        state.assign(new ChallengeCourse("opaque-1", "Programming Foundation",
                QuestionTopic.PROGRAMMING_FOUNDATION, new ChallengeResult(2, 3)));
        state.energy = 0;
        command(Action.SELECT); command(Action.START);
        assertEquals(Stage.RESULT, recorder.latest.stage);
        assertEquals(2, recorder.latest.result.getPreparationPoints());
        assertEquals(0, state.starts);
        assertEquals(0, loads);
    }

    @Test void exactEnergyCostIsAllowedAndRemainsZeroAfterCompletion() throws Exception {
        state.energy = 20;
        start();
        assertEquals(0, state.energy);
        finish();
        assertEquals(0, recorder.latest.overview.getEnergy());
        assertEquals(2, state.awardedPoints);
    }

    @Test void rejectsUnassignedCoursesMissingMajorAndInsufficientEnergy() throws Exception {
        assertThrows(ChallengeStateException.class, () -> interactor.execute(new ChallengeInputData(
                Action.SELECT, "not-enrolled", "", 0, -1, false)));
        state.energy = 19;
        assertThrows(ChallengeStateException.class, () -> command(Action.SELECT));
        assertEquals(19, state.energy);
        state = new FakeChallengeState();
        interactor = create((t, p, c) -> { fail("Do not load without enrolment"); return List.of(); });
        command(Action.OPEN);
        assertTrue(recorder.latest.overview.getCourses().isEmpty());
        assertThrows(ChallengeStateException.class, () -> command(Action.SELECT));
    }

    @Test void apiFailureAndInsufficientQuestionDataNeverChargeEnergy() throws Exception {
        for (QuestionGateway gateway : List.<QuestionGateway>of(
                (t, p, c) -> { throw new QuestionLoadException("offline"); },
                (t, p, c) -> List.of(),
                (t, p, c) -> List.of(testQuestions().get(0), testQuestions().get(0), testQuestions().get(0)))) {
            interactor = create(gateway);
            command(Action.SELECT);
            assertThrows(QuestionLoadException.class, () -> command(Action.START));
            assertEquals(100, state.energy);
            assertEquals(0, state.starts);
            assertNull(state.activeAttempt);
        }
    }

    @Test void startRevalidatesEnergyAndActivityAfterRemoteLoad() throws Exception {
        interactor = create((t, p, c) -> { state.energy = 0; return testQuestions(); });
        command(Action.SELECT);
        assertThrows(ChallengeStateException.class, () -> command(Action.START));
        assertEquals(0, state.starts);
        assertEquals(0, recorder.latest.overview.getEnergy());
        state.energy = 100;
        state.activeAttempt = "exam-owned";
        interactor = create((t, p, c) -> testQuestions());
        command(Action.SELECT);
        assertThrows(ChallengeStateException.class, () -> command(Action.START));
        assertEquals(100, state.energy);
    }

    @Test void failedCompletionKeepsFinalFeedbackAndCanRetryOnce() throws Exception {
        start();
        answer(0); command(Action.NEXT); answer(1); command(Action.NEXT); answer(0);
        state.failCompletion = true;
        assertThrows(ChallengeStateException.class, () -> command(Action.NEXT));
        assertEquals(Stage.FEEDBACK, recorder.latest.stage);
        assertEquals(0, state.completions);
        assertNotNull(state.activeAttempt);
        state.failCompletion = false;
        command(Action.NEXT); command(Action.NEXT);
        assertEquals(1, state.completions);
        assertEquals(3, state.awardedPoints);
    }

    @Test void concurrentDuplicateStartIsSerialized() throws Exception {
        command(Action.SELECT);
        java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(2);
        try {
            java.util.concurrent.Callable<Void> task = () -> { command(Action.START); return null; };
            for (java.util.concurrent.Future<Void> future : pool.invokeAll(List.of(task, task))) { future.get(); }
            assertEquals(1, state.starts);
            assertEquals(80, state.energy);
            assertEquals(1, loads);
        }
        finally { pool.shutdownNow(); }
    }
}
