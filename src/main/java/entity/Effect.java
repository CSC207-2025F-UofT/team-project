package entity;

/** An effect validates without mutation, then applies through a narrow state capability. */
public interface Effect {
    void validate(RoomReadState facts);

    void apply(RoomMutation target);
}
