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

    /**
     * Returns the title of the book that was added.
     *
     * @return the added book title
     */
    public String getTitle() {
        return book.getTitle();
    }

    /**
     * Returns the author of the book that was added.
     *
     * @return the added book author
     */
    public String getAuthor() {
        return book.getAuthor();
    }
}