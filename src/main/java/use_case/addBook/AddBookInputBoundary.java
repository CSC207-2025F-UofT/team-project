package use_case.addBook;

/**
 * Input Boundary for actions related to Adding a book to bookshelf
 */
public interface AddBookInputBoundary {
    /**
     * Executes the Add Book usee case
     * @param addBookInputData the input data
     */
    void execute(AddBookInputData addBookInputData);
}
