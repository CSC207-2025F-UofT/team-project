package entity;

import java.util.Objects;
import java.util.Optional;

/** Immutable story configuration. */
public final class TriviaSpec {
    public final TriviaSource source;
    public final Optional<String> category;
    public final Optional<String> difficulty;

    public TriviaSpec(TriviaSource source,
                      Optional<String> category,
                      Optional<String> difficulty) {
        this.source = Objects.requireNonNull(source, "source");
        this.category = Objects.requireNonNull(category, "category");
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty");
    }
}
