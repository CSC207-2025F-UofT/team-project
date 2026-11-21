package use_case.addBook;

import entity.Book;
import entity.Bookshelf;

/**
 * Data Access Interface to handle the retrieving and saving of a bookshelf
 */
public interface BookshelfDataAccessInterface {

    Bookshelf getBookshelf();

    boolean existsOnBookshelf(Book book);

    void saveBookshelf(Bookshelf bookshelf);
}