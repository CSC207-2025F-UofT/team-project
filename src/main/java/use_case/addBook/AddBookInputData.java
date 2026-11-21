package use_case.addBook;

import entity.Book;

/**
 * Stores the input data required to add a book
 * to the user's personal bookshelf.
 */
public class AddBookInputData {
    private final Book book;
    /**
     * Creates an AddBookInputData object.
     * @param book the selected book
     */
    public AddBookInputData(Book book) {
        this.book = book;
    }

    /**
    * Returns the selected book.
    *
    * @return the selected book.
     */
    public Book getBook() {
            return this.book;
        }
}
