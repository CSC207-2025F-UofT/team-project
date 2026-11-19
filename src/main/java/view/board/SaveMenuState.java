package view.board;

import java.util.Objects;

/** View-facing snapshot of the current canvas save state (no persistence logic here). */
public record SaveMenuState(String name, Phase phase) {
    public enum Phase { NEW, MODIFIED, SAVING, SAVED }

    public SaveMenuState {
        name = Objects.requireNonNull(name).trim();
        phase = Objects.requireNonNull(phase);
    }

    public static SaveMenuState newCanvas() {
        return new SaveMenuState("Untitled canvas", Phase.NEW);
    }
    /** Short display label only; the real saved name is never changed or truncated. */
    public String compactName() {
        if (name.codePointCount(0, name.length()) <= 22) return name;
        int end = name.offsetByCodePoints(0, 21);
        return name.substring(0, end) + "…";
    }

    public boolean canSaveInPlace() { return phase == Phase.MODIFIED; }
    public boolean hasSavedFile() { return phase != Phase.NEW; }
}
