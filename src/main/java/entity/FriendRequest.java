package entity;

import java.time.Instant;
import java.util.UUID;

public class FriendRequest {
    private final UUID friendReqID;
    private final UUID senderID;
    private final UUID targetID;
    private String status; // PENDING, ACCEPTED, DECLINED or CANCELLED
    private final Instant createdAt;

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
