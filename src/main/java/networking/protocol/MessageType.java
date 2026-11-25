package networking.protocol;

public enum MessageType {
    CREATE_ROOM,
    JOIN_ROOM,
//    LIST_ROOMS, (Not a feature, at least yet)
//    ROOM_UPDATED, (This will be done by JOIN_ROOM 'on the way back' from the server)
    MATCH_STARTED,

    CAST_SPELL,
//    CAST_INTENT, (This will be done by CAST_SPELL 'on the way back' from the server)
    RESPOND_TO_SPELL,
    RESOLVE_CAST,

    MOVE,
//    RESOLVE_MOVE, (This will be done by MOVE 'on the way back' from the server)

    END_TURN,
//    TURN_CHANGED, (This will be done by END_TURN 'on the way back' from the server)

    PLAYER_DISCONNECTED
}