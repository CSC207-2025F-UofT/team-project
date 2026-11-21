package data_access;

import entity.Book;
import use_case.addBook.BookSearchDataAccessInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory DAO for books that have been searched (and viewed) but not added to the bookshelf.
 * Books are kept only for the current session and are lost when the app closes.
 * Books are identified by their OpenLibraryID.
 *
 * When the user chooses to add a book, the Add Book use case looks the book up here
 * and saves it through {@link FileBookshelfDataAccessObject}.
 */
public class InMemoryBookDataAccessObject implements BookSearchDataAccessInterface {
    private final Map<String, Book> books = new LinkedHashMap<>();

    /**
     * Stores a book, replacing any existing book with the same OpenLibraryID.
     * @param book the book to store
     */
    public void save(Book book) {
        books.put(book.getOpenLibraryId(), book);
    }

    /**
     * Stores all books from a search, replacing the previous search results.
     * @param results the books returned by the latest search
     */
    public void saveSearchResults(List<Book> results) {
        books.clear();
        for (Book book : results) {
            save(book);
        }
    }

    /**
     * Returns the stored searched book with the same OpenLibraryID as the given book.
     * @param book the book to look up
     * @return the stored book, or null if no book with that OpenLibraryID is stored
     */
    @Override
    public Book getBook(Book book) {
        return book == null ? null : books.get(book.getOpenLibraryId());
    }

    /**
     * Checks whether a book with the given OpenLibraryID is stored.
     * @param openLibraryId the OpenLibraryID of the book
     * @return true if the book is stored, false otherwise
     */
    public boolean existsByOpenLibraryId(String openLibraryId) {
        return books.containsKey(openLibraryId);
    }

    /**
     * Removes the book with the given OpenLibraryID, if it is stored.
     * @param openLibraryId the OpenLibraryID of the book to remove
     */
    public void remove(String openLibraryId) {
        books.remove(openLibraryId);
    }

    /**
     * Returns all stored books in the order they were added.
     * @return a list of all stored books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    /**
     * Removes all stored books.
     */
    public void clear() {
        books.clear();
    }
}
