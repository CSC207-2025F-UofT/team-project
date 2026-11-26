package entity;

import java.util.Objects;

/** Immutable command feedback, independent of Swing and output DTOs. */
public final class DomainResult {
    private final ResultCode code;
    private final boolean changed;
    private final String resultText;

    public DomainResult(ResultCode code, boolean changed, String resultText) {
        this.code = Objects.requireNonNull(code, "code");
        this.changed = changed;
        this.resultText = Objects.requireNonNull(resultText, "resultText");
    }

    public ResultCode code() { return code; }
    public boolean changed() { return changed; }
    public String resultText() { return resultText; }
}
