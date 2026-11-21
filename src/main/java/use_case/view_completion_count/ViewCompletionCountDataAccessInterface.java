package use_case.view_completion_count;

import entity.Board;
/**
 * Data access interface for the ViewCompletionCount use case.
 * Implemented by FileBoardDataAccessObject in the frameworks layer.
 */
public interface ViewCompletionCountDataAccessInterface {
    /**
     * Retrieves a board by its ID.
     * @param boardId the ID of the board to retrieve
     * @return the Board, or null if not found
     */
    Board getBoard(String boardId);
}