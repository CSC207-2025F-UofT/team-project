package use_case.addBook;

import entity.Book;

/**
 * Stores the output data produced after successfully
 * adding a book to the user's bookshelf.
 */
public class AddBookOutputData {

    private final Book book;

    /**
     * Creates an AddBookOutputData object.
     *
     * @param book the book that was added to the bookshelf
     */
    public AddBookOutputData(Book book) {
        this.book = book;
    }

    /**
     * Returns the book that was added.
     *
     * @return the added book
     */
    public Book getBook() {
        return book;
    }
}