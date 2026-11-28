package entity;

import java.util.Objects;
import java.util.Optional;

/** Immutable story configuration. */
public final class TriviaLockDefinition {
    public final String id;
    public final String description;
    public final String successText;
    public final TriviaSpec spec;
    public final Optional<QuestionDefinition> initialTrivia;

    public TriviaLockDefinition(String id,
                                String description,
                                String successText,
                                TriviaSpec spec,
                                Optional<QuestionDefinition> initialTrivia) {
        this.id = TemplateText.require(id, "id");
        this.description = TemplateText.require(description, "description");
        this.successText = TemplateText.require(successText, "successText");
        this.spec = Objects.requireNonNull(spec, "spec");
        this.initialTrivia = Objects.requireNonNull(initialTrivia, "initialTrivia");
        if ((spec.source == TriviaSource.TEMPLATE) != initialTrivia.isPresent()) {
            throw new IllegalArgumentException("Only TEMPLATE locks require an authored question");
        }
    }
}
