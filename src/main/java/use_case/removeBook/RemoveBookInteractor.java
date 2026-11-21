package use_case.removeBook;

import entity.Book;
import entity.Bookshelf;

/**
 * Removes a book from the user's bookshelf.
 * Checks that the book is on the bookshelf, removes it, saves the bookshelf, and reports the result.
 */
public class RemoveBookInteractor implements RemoveBookInputBoundary {
    private final RemoveBookDataAccessInterface bookshelfDataAccessObject;
    private final RemoveBookOutputBoundary removeBookPresenter;

    /**
     * Constructs a RemoveBookInteractor.
     * @param bookshelfDataAccessObject provides access to the bookshelf
     * @param removeBookPresenter prepares the output for the user
     */
    public RemoveBookInteractor(RemoveBookDataAccessInterface bookshelfDataAccessObject,
                                RemoveBookOutputBoundary removeBookPresenter) {
        this.bookshelfDataAccessObject = bookshelfDataAccessObject;
        this.removeBookPresenter = removeBookPresenter;
    }

    /**
     * Attempts to remove the selected book from the user's bookshelf.
     * @param inputData contains the OpenLibraryID of the book to remove
     */
    @Override
    public void execute(RemoveBookInputData inputData) {
        String openLibraryId = inputData.getOpenLibraryId();
        Book book = bookshelfDataAccessObject.getBook(openLibraryId);

        if (book == null) {
            removeBookPresenter.prepareFailView("This book is not on your bookshelf.");
            return;
        }

        Bookshelf bookshelf = bookshelfDataAccessObject.getBookshelf();
        bookshelf.removeBook(book);
        bookshelfDataAccessObject.saveBookshelf(bookshelf);

        removeBookPresenter.prepareSuccessView(
                new RemoveBookOutputData(openLibraryId, book.getTitle()));
    }
}
