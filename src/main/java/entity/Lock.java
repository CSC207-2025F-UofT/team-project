package entity;

/** Shared lock identity and progress. Subtypes own their validation rules. */
public abstract class Lock {
    private final String id;
    private final String description;
    private final String successText;
    private final LockKind kind;
    private boolean unlocked;

    protected Lock(String id, String description, String successText, LockKind kind) {
        if (id == null || id.isBlank() || description == null || description.isBlank()
                || successText == null || successText.isBlank() || kind == null) {
            throw new IllegalArgumentException("Lock identity, description, success text and kind are required");
        }
        this.id = id;
        this.description = description;
        this.successText = successText;
        this.kind = kind;
    }

    public String id() { return id; }
    public String description() { return description; }
    public String successText() { return successText; }
    public LockKind kind() { return kind; }
    public boolean isUnlocked() { return unlocked; }
    protected final void markUnlocked() { unlocked = true; }
}
