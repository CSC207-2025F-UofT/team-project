package data_access;

import entity.Book;
import entity.Bookshelf;
import use_case.addBook.BookshelfDataAccessInterface;

/**
 * File-based DAO for the user's bookshelf.
 * The bookshelf holds every book saved in its {@link FileBookDataAccessObject}:
 * the bookshelf is rebuilt from the books file when it is requested,
 * and saving the bookshelf writes its books back to that file.
 * Books are identified by their OpenLibraryID.
 */
public class FileBookshelfDataAccessObject implements BookshelfDataAccessInterface {
    private final FileBookDataAccessObject bookDataAccessObject;

    /**
     * Constructs this DAO on top of the DAO that stores the saved books.
     * @param bookDataAccessObject the DAO that saves the bookshelf's books to a file
     */
    public FileBookshelfDataAccessObject(FileBookDataAccessObject bookDataAccessObject) {
        this.bookDataAccessObject = bookDataAccessObject;
    }

    /**
     * Returns the bookshelf, containing every saved book in the order they were added.
     * @return the user's bookshelf
     */
    @Override
    public Bookshelf getBookshelf() {
        Bookshelf bookshelf = new Bookshelf();
        for (Book book : bookDataAccessObject.getAllBooks()) {
            bookshelf.addBook(book);
        }
        return bookshelf;
    }

    /**
     * Checks whether a book with the same OpenLibraryID is on the bookshelf.
     * @param book the book to look for
     * @return true if the book is on the bookshelf, false otherwise
     */
    @Override
    public boolean existsOnBookshelf(Book book) {
        return book != null && bookDataAccessObject.existsByOpenLibraryId(book.getOpenLibraryId());
    }

    /**
     * Saves the bookshelf, so the books file holds exactly the books on it.
     * Books that were not shelved yet are marked as shelved, which sets their date added.
     * @param bookshelf the bookshelf to save
     */
    @Override
    public void saveBookshelf(Bookshelf bookshelf) {
        for (Book book : bookshelf.getBooks()) {
            if (!book.isShelved()) {
                book.setShelved(true);
            }
        }
        bookDataAccessObject.saveAll(bookshelf.getBooks());
    }

    /**
     * Returns the book on the bookshelf with the given OpenLibraryID.
     * @param openLibraryId the OpenLibraryID of the book
     * @return the book, or null if it is not on the bookshelf
     */
    public Book getBook(String openLibraryId) {
        return bookDataAccessObject.getBook(openLibraryId);
    }

    /**
     * Saves changes to a book already on the bookshelf, such as a new review.
     * @param book the updated book
     */
    public void updateBook(Book book) {
        bookDataAccessObject.save(book);
    }

    /**
     * Removes the book with the given OpenLibraryID from the bookshelf and the books file.
     * @param openLibraryId the OpenLibraryID of the book to remove
     */
    public void removeBook(String openLibraryId) {
        Book book = bookDataAccessObject.getBook(openLibraryId);
        if (book != null) {
            book.setShelved(false);
            bookDataAccessObject.remove(openLibraryId);
        }
    }
}
