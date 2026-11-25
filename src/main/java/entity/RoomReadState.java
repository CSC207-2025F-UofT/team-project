package entity;

/** Narrow, read-only room facts available to conditions and effect validation. */
public interface RoomReadState {
    String objectState(String objectId, String key);

    boolean acceptsObjectState(String objectId, String key, String value);
}
