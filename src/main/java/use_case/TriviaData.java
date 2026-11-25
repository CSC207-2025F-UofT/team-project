package use_case;

import java.util.List;
import java.util.Objects;

/** Public question content; expected answers never enter the view. */
public final class TriviaData {
    private final String lockId;
    private final String prompt;
    private final List<String> choices;

    public TriviaData(String lockId, String prompt, List<String> choices) {
        this.lockId = Objects.requireNonNull(lockId, "lockId");
        this.prompt = Objects.requireNonNull(prompt, "prompt");
        this.choices = List.copyOf(choices);
    }

    public String lockId() { return lockId; }
    public String prompt() { return prompt; }
    public List<String> choices() { return choices; }
}
