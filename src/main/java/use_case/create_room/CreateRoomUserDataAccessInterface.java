package use_case.create_room;

import entity.Room;

import java.util.UUID;

/**
 * Gateways to persist/fetch room for create flow.
 */
public interface CreateRoomUserDataAccessInterface {
    //Methods to save new room, ensure code uniqueness, generate tokens, etc.
    boolean existsByRoomCode(String roomCode);

    void save(Room room);

    void setCurrentRoom(String roomName);

    boolean verifyRoomUniquenessPerUser(String hostId);

    default String generateUniqueRoomCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        } while (existsByRoomCode(code));
        return code;
    }

    default String generateToken() {
        return UUID.randomUUID().toString();
    }
}

