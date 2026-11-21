package use_case.addBook;

import entity.Book;

public interface BookshelfDataAccessInterface {

    boolean existsByOpenLibraryId(String openLibraryId);

    void saveBook(Book book);
}