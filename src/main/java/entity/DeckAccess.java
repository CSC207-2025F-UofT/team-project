package entity;

import java.util.Objects;

public class DeckAccess {
    private final int deckId;
    private final int userId;
    private AccessRole role;

    public DeckAccess(int deckId, int userId, AccessRole role) {
        this.deckId = deckId;
        this.userId = userId;
        this.role = Objects.requireNonNull(role, "Role is required");
    }

    public void changeRole(AccessRole role) {
        this.role = Objects.requireNonNull(role, "Role is required");
    }

    public boolean canEdit() {
        //return this.role == AccessRole.EDITOR || this.role == AccessRole.OWNER;
        return false;
    }

    public int getDeckId() {
        return deckId;
    }

    public int getUserId() {
        return userId;
    }

    public AccessRole getRole() {
        return role;
    }
}