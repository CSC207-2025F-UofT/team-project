package entity;

import java.util.Objects;
import java.util.Optional;

/** Independent exit question gate; initialized questions are retained across attempts. */
public final class TriviaLock extends Lock {
    private Optional<Trivia> trivia;

    public TriviaLock(String id, String description, String successText, Optional<Trivia> trivia) {
        super(id, description, successText, LockKind.TRIVIA);
        this.trivia = Objects.requireNonNull(trivia, "trivia");
    }

    public Optional<Trivia> getTrivia() { return trivia; }
    public boolean needsTrivia() { return trivia.isEmpty(); }

    void initializeTrivia(Trivia question) {
        Objects.requireNonNull(question, "question");
        if (trivia.isEmpty()) trivia = Optional.of(question);
    }

    DomainResult submitAnswer(String answer) {
        if (isUnlocked()) return new DomainResult(ResultCode.ALREADY_UNLOCKED, false, "The question is already passed.");
        if (trivia.isEmpty()) return new DomainResult(ResultCode.TRIVIA_UNINITIALIZED, false, "Open the question before answering.");
        if (!trivia.get().isCorrect(answer)) return new DomainResult(ResultCode.WRONG_ANSWER, false, "That answer is incorrect.");
        markUnlocked();
        return new DomainResult(ResultCode.SUCCESS, true, successText());
    }
}
