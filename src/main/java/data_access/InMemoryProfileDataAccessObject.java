package data_access;

import entity.ChildProfile;
import entity.CommunicationBoard;
import entity.CommunicationMessage;
import use_case.communicate_request.CommunicateRequestDataAccessInterface;
import use_case.core_message.CoreMessageDataAccessInterface;
import use_case.customize_board.CustomizeBoardDataAccessInterface;
import use_case.view_history.ViewHistoryDataAccessInterface;

import java.util.List;
import java.util.Objects;

/**
 * Keeps the child's profile in memory only (nothing is saved to a file).
 * Used for development and testing until JSON storage is ready.
 */
public class InMemoryProfileDataAccessObject implements
        CommunicateRequestDataAccessInterface,
        CoreMessageDataAccessInterface,
        CustomizeBoardDataAccessInterface,
        ViewHistoryDataAccessInterface {

    private final ChildProfile profile;

    public InMemoryProfileDataAccessObject(ChildProfile profile) {
        this.profile = Objects.requireNonNull(profile, "profile cannot be null");
    }

    @Override
    public CommunicationBoard getBoard() {
        return profile.getBoard();
    }

    @Override
    public void saveBoard(CommunicationBoard board) {
        // The profile already holds this board object, so there is nothing else to save.
    }

    @Override
    public void saveMessage(CommunicationMessage message) {
        profile.addMessage(message);
    }

    @Override
    public List<CommunicationMessage> getHistory() {
        return profile.getHistory();
    }
}
