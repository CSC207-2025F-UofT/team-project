package use_case.addBook;

import entity.Book;
import entity.Bookshelf;

/**
 * Coordinates the process of adding a book to the user's bookshelf.
 *
 * Retrieves the selected book, checks whether
 * it already exists in the bookshelf, and saves it if it is not a duplicate.
 */
public class   AddBookInteractor implements AddBookInputBoundary {

    private final BookshelfDataAccessInterface bookshelfDataAccessObject;
    private final AddBookOutputBoundary addBookPresenter;

    /**
     * Constructs an AddBookInteractor.
     *
     * @param bookshelfDataAccessObject provides access to bookshelf data
     * @param addBookPresenter prepares the output for the user
     */
    public AddBookInteractor(
            BookshelfDataAccessInterface bookshelfDataAccessObject,
            AddBookOutputBoundary addBookPresenter) {

        this.bookshelfDataAccessObject = bookshelfDataAccessObject;
        this.addBookPresenter = addBookPresenter;
    }

    /**
     * Attempts to add a selected book to the user's bookshelf.
     *
     * @param inputData contains the selected book as an object from the search.
     */
    @Override
    public void execute(AddBookInputData inputData) {

        // Retrieve the existing bookshelf.
        Bookshelf bookshelf = bookshelfDataAccessObject.getBookshelf();

        // Check whether the book could be retrieved.
        if (inputData.getBook() == null) {
            addBookPresenter.prepareFailView(
                    "Book could not be found.");
            return;
        }

        // Check whether the book already exists in the bookshelf.
        if (bookshelfDataAccessObject.existsOnBookshelf(
                inputData.getBook())) {

            addBookPresenter.prepareFailView(
                    "This book is already on your bookshelf.");
            return;
        }

        // Add the book to the user's bookshelf.
        bookshelf.addBook(inputData.getBook());
        bookshelfDataAccessObject.saveBookshelf(bookshelf);

        // Notify the presenter that the book was successfully added.
        AddBookOutputData outputData = new AddBookOutputData(inputData.getBook());

        addBookPresenter.prepareSuccessView(outputData);
    }
}