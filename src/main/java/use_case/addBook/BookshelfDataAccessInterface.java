package use_case.addBook;

import entity.Book;
import entity.Bookshelf;

public interface BookshelfDataAccessInterface {

    Bookshelf getBookshelf();

    boolean existsOnBookshelf(Book book);

    void saveBookshelf(Bookshelf bookshelf);
}