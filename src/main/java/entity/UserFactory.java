package entity;

/** Generates sequential user IDs for concrete user factories. */
public abstract class UserFactory {
    private int nextUniqueId;

    protected UserFactory() {
        this(1);
    }

    /** Accepts the next available ID, including a value restored from storage. */
    protected UserFactory(int nextUniqueId) {
        if (nextUniqueId <= 0) {
            throw new IllegalArgumentException("The next user unique ID must be positive.");
        }

        this.nextUniqueId = nextUniqueId;
    }

    protected int generateNextUniqueId() {
        if (nextUniqueId == Integer.MAX_VALUE) {
            throw new IllegalStateException("No more user IDs can be generated.");
        }

        return nextUniqueId++;
    }

    /** Returns the counter for saving without consuming an ID. */
    public int getNextUniqueId() {
        return nextUniqueId;
    }
}