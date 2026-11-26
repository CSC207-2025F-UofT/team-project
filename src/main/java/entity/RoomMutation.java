package entity;

/** The only mutation capability exposed to a prevalidated object-state effect. */
public interface RoomMutation {
    void setObjectState(String objectId, String key, String value);
}
