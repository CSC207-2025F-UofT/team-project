package use_case.removeBook;

/**
 * Input Boundary for removing a book from the bookshelf.
 */
public interface RemoveBookInputBoundary {
    /**
     * Executes the Remove Book use case.
     * @param removeBookInputData the input data
     */
    void execute(RemoveBookInputData removeBookInputData);
}
