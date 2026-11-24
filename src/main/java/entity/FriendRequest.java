package entity;

import java.time.Instant;
import java.util.UUID;

/**
 * A request from one user to become friends with another. Its status changes
 * as the request is answered; when it is accepted, a {@link Friendship} is created.
 */
public class FriendRequest {
    private final UUID friendReqID;
    private final UUID senderID;
    private final UUID targetID;
    private String status; // PENDING, ACCEPTED, DECLINED or CANCELLED
    private final Instant createdAt;

    /**
     * Creates a friend request.
     *
     * @param friendReqID the unique ID of the request
     * @param senderID    the ID of the user who sent the request
     * @param targetID    the ID of the user who received the request
     * @param status      one of PENDING, ACCEPTED, DECLINED or CANCELLED
     * @param createdAt   when the request was sent
     */
    public FriendRequest(UUID friendReqID, UUID senderID, UUID targetID, String status, Instant createdAt) {
        this.friendReqID = friendReqID;
        this.senderID = senderID;
        this.targetID = targetID;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getFriendReqID() { return friendReqID; }
    public UUID getSenderID() { return senderID; }
    public UUID getTargetID() { return targetID; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
}
