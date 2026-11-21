package use_case.addBook;

import entity.Book;

public interface BookSearchDataAccessInterface {
    Book getBook(Book book);
}