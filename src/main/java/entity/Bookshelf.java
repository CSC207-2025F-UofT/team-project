package entity;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a user's personal bookshelf, which maintains a collection
 * of books that the user has added to their library.
 *
 * The bookshelf provides functionality to retrieve, add, and remove
 * books from the user's collection.
 */
public class Bookshelf {
    private final ArrayList<Book> books;

    /**
     * Constructs an empty bookshelf.
     */
    public Bookshelf(){
        this.books = new ArrayList<>();
    }

    /**
     * Returns the collection of books currently on the bookshelf.
     *
     * @return an ArrayList containing all books on the bookshelf
     */
    public ArrayList<Book> getBooks(){
        return books;
    }

    /**
     * Adds a book to the bookshelf.
     *
     * @param book the book to be added to the bookshelf
     **/
    public void addBook(Book book){

    }

    /**
     * Removes a book from the bookshelf.
     *
     * @param book the book to be removed from the bookshelf
     */
    public void removeBook(Book book){

    }

    /**
     * Sorts the bookshelf by a certain comparator.
     *
     * @param compareBy the comparator to sort the list by
     */
    public void sortBooks(Comparator compareBy){

    }

}