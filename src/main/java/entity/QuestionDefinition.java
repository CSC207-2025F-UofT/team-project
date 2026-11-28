package entity;

import java.util.List;

/** Immutable story configuration. */
public final class QuestionDefinition {
    public final String prompt;
    public final List<String> choices;
    public final String expectedAnswer;

    public QuestionDefinition(String prompt,
                              List<String> choices,
                              String expectedAnswer) {
        this.prompt = TemplateText.require(prompt, "prompt");
        this.choices = List.copyOf(choices);
        this.expectedAnswer = TemplateText.require(expectedAnswer, "expectedAnswer");
    }
}
