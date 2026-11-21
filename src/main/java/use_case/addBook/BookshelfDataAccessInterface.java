package use_case.addBook;

import entity.Book;

public interface BookshelfDataAccessInterface {

    boolean existsByOpenLibraryId(Book book);

    void saveBook(Book book);
}