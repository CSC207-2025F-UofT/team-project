package use_case.challenge;

import entity.ChallengeResult;
import entity.Question;
import entity.QuestionSession;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import use_case.question.QuestionGateway;
import use_case.question.QuestionLoadException;
import use_case.question.QuestionPurpose;
import static use_case.challenge.ChallengeOutputData.Stage;

/** UC2 rules only: no Swing, JSON, HTTP, scraped catalogue or concrete shared state. */
public final class ClassChallengeInteractor implements ChallengeInputBoundary {
    public static final int QUESTION_COUNT = 3;
    public static final int ENERGY_COST = 20;

    private final ChallengeStateGateway state;
    private final QuestionGateway questions;
    private final ChallengeOutputBoundary output;
    private ChallengeOverview overview;
    private ChallengeCourse course;
    private QuestionSession session;
    private String attemptId = "";
    private int questionIndex;
    private Stage stage = Stage.COURSES;
    private ChallengeResult result;

    public ClassChallengeInteractor(ChallengeStateGateway state, QuestionGateway questions,
                                   ChallengeOutputBoundary output) {
        this.state = Objects.requireNonNull(state);
        this.questions = Objects.requireNonNull(questions);
        this.output = Objects.requireNonNull(output);
    }

    @Override
    public synchronized void execute(ChallengeInputData input)
            throws QuestionLoadException, ChallengeStateException {
        if (overview == null) { overview = state.loadOverview(); }
        try {
            dispatch(input);
        }
        catch (QuestionLoadException | ChallengeStateException exception) {
            try {
                overview = state.loadOverview();
                present();
            }
            catch (ChallengeStateException refreshFailure) {
                exception.addSuppressed(refreshFailure);
            }
            throw exception;
        }
        present();
    }

    private void dispatch(ChallengeInputData input) throws QuestionLoadException, ChallengeStateException {
        switch (input.action) {
            case OPEN:
                if (session == null) { open(); }
                break;
            case SELECT:
                select(input.courseId);
                break;
            case START:
                start(input.courseId);
                break;
            case ANSWER:
                answer(input);
                break;
            case NEXT:
                next(input);
                break;
            case LEAVE:
                leave(input.confirmed);
                break;
            default:
                throw new IllegalArgumentException("Unsupported challenge action");
        }
    }

    private void open() throws ChallengeStateException {
        overview = state.loadOverview();
        course = null;
        result = null;
        attemptId = "";
        stage = Stage.COURSES;
    }

    private void select(String courseId) throws ChallengeStateException {
        if (session != null) {
            throw new ChallengeStateException("Finish or leave the current challenge first.");
        }
        overview = state.loadOverview();
        ChallengeCourse selected = findCourse(courseId);
        if (selected.getResult().isPresent()) {
            course = selected;
            result = selected.getResult().get();
            stage = Stage.RESULT;
        }
        else {
            requireEnergy();
            course = selected;
            result = null;
            stage = Stage.CONFIRM;
        }
    }

    private void start(String courseId) throws QuestionLoadException, ChallengeStateException {
        if (session != null || stage == Stage.RESULT) {
            return;
        }
        if (stage != Stage.CONFIRM || course == null || !course.getId().equals(courseId)) {
            throw new ChallengeStateException("Select a course and confirm participation first.");
        }
        overview = state.loadOverview();
        course = findCourse(courseId);
        if (course.getResult().isPresent()) {
            result = course.getResult().get();
            stage = Stage.RESULT;
            return;
        }
        requireEnergy();
        List<Question> loaded = questions.getQuestions(course.getTopic(), QuestionPurpose.PRACTICE,
                QUESTION_COUNT);
        if (loaded == null || loaded.size() != QUESTION_COUNT) {
            throw new QuestionLoadException("The question service did not provide three questions.");
        }
        final QuestionSession candidate;
        try { candidate = new QuestionSession(loaded); }
        catch (RuntimeException exception) {
            throw new QuestionLoadException("The question service returned invalid questions.", exception);
        }
        String newAttemptId = UUID.randomUUID().toString();
        // The shared adapter revalidates state after the network call and charges atomically.
        overview = state.startChallenge(courseId, newAttemptId, ENERGY_COST);
        session = candidate;
        attemptId = newAttemptId;
        questionIndex = 0;
        stage = Stage.QUESTION;
    }

    private void answer(ChallengeInputData input) throws ChallengeStateException {
        if (!matches(input) || stage != Stage.QUESTION) { return; }
        if (input.answerIndex < 0 || input.answerIndex >= currentQuestion().getOptions().size()) {
            throw new ChallengeStateException("Select an answer before submitting.");
        }
        session.answer(questionIndex, input.answerIndex);
        stage = Stage.FEEDBACK;
    }

    private void next(ChallengeInputData input) throws ChallengeStateException {
        if (!matches(input) || stage != Stage.FEEDBACK) { return; }
        if (questionIndex + 1 < session.getQuestions().size()) {
            questionIndex++;
            stage = Stage.QUESTION;
        }
        else {
            ChallengeResult completed = new ChallengeResult(session.getCorrectCount(), QUESTION_COUNT);
            // Keep the attempt and final feedback on failure so Finish can safely be retried.
            overview = state.completeChallenge(course.getId(), attemptId, completed);
            result = completed;
            session = null;
            stage = Stage.RESULT;
        }
    }

    private void leave(boolean confirmed) throws ChallengeStateException {
        if (session != null) {
            if (!confirmed) {
                throw new ChallengeStateException("Confirm leaving: spent energy will not be refunded.");
            }
            overview = state.abandonChallenge(attemptId);
            session = null;
        }
        if (overview == null) { overview = state.loadOverview(); }
        stage = Stage.LEFT;
    }

    private boolean matches(ChallengeInputData input) {
        return session != null && attemptId.equals(input.attemptId)
                && questionIndex == input.questionIndex;
    }

    private ChallengeCourse findCourse(String id) throws ChallengeStateException {
        for (ChallengeCourse assigned : overview.getCourses()) {
            if (assigned.getId().equals(id)) { return assigned; }
        }
        throw new ChallengeStateException("Choose a major at the Registrar before starting an assigned course.");
    }

    private void requireEnergy() throws ChallengeStateException {
        if (overview.getEnergy() < ENERGY_COST) {
            throw new ChallengeStateException("Not enough energy. Rest at Residence and try again.");
        }
    }

    private Question currentQuestion() { return session.getQuestions().get(questionIndex); }

    private void present() {
        String text = "";
        List<String> options = List.of();
        String feedback = "";
        if (session != null) {
            Question question = currentQuestion();
            text = question.getText();
            options = question.getOptions();
            if (stage == Stage.FEEDBACK) {
                boolean correct = question.isCorrect(session.getAnswer(questionIndex));
                feedback = (correct ? "Correct! " : "Incorrect. ") + "Correct answer: "
                        + (char) ('A' + question.getCorrectAnswer()) + ". "
                        + question.getOptions().get(question.getCorrectAnswer());
                if (question.getExplanation().isPresent()) {
                    feedback += "\n" + question.getExplanation().get();
                }
            }
        }
        output.present(new ChallengeOutputData(stage, overview, course, attemptId, questionIndex,
                QUESTION_COUNT, ENERGY_COST, text, options, feedback, result));
    }
}
