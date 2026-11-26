package use_case.communicate_request;

import entity.CommunicationBoard;

/**
 * Data required by UC1 - Build a Request.
 */
public interface CommunicateRequestDataAccessInterface {

    /**
     * Returns the child's current communication board.
     */
    CommunicationBoard getBoard();
}
