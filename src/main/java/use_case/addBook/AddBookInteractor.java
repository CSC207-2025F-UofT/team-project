package use_case.addBook;

import entity.Book;

/**
 * Coordinates the process of adding a book to the user's bookshelf.
 *
 * Retrieves the selected book using its Open Library ID, checks whether
 * it already exists in the bookshelf, and saves it if it is not a duplicate.
 */
public class AddBookInteractor implements AddBookInputBoundary {

    private final BookSearchDataAccessInterface bookDataAccessObject;
    private final BookshelfDataAccessInterface bookshelfDataAccessObject;
    private final AddBookOutputBoundary addBookPresenter;

    /**
     * Constructs an AddBookInteractor.
     *
     * @param bookDataAccessObject provides access to book information
     * @param bookshelfDataAccessObject provides access to bookshelf data
     * @param addBookPresenter prepares the output for the user
     */
    public AddBookInteractor(
            BookSearchDataAccessInterface bookDataAccessObject,
            BookshelfDataAccessInterface bookshelfDataAccessObject,
            AddBookOutputBoundary addBookPresenter) {

        this.bookDataAccessObject = bookDataAccessObject;
        this.bookshelfDataAccessObject = bookshelfDataAccessObject;
        this.addBookPresenter = addBookPresenter;
    }

    /**
     * Attempts to add a selected book to the user's bookshelf.
     *
     * @param inputData contains the Open Library ID of the selected book
     */
    @Override
    public void execute(AddBookInputData inputData) {

        // Retrieve the selected book using its Open Library ID.
        Book book = bookDataAccessObject.getBookByOpenLibraryId(
                inputData.getOpenLibraryId());

        // Check whether the book could be retrieved.
        if (book == null) {
            addBookPresenter.prepareFailView(
                    "Book could not be found.");
            return;
        }

        // Check whether the book already exists in the bookshelf.
        if (bookshelfDataAccessObject.existsByOpenLibraryId(
                inputData.getOpenLibraryId())) {

            addBookPresenter.prepareFailView(
                    "This book is already on your bookshelf.");
            return;
        }

        // Add the book to the user's bookshelf.
        bookshelfDataAccessObject.saveBook(book);

        // Notify the presenter that the book was successfully added.
        AddBookOutputData outputData =
                new AddBookOutputData(book, false);

        addBookPresenter.prepareSuccessView(outputData);
    }
}