package entity;

import java.time.LocalDateTime;
/**
 * Factory for creating Book objects.
 */
public class BookFactory {
    /**
     * Creates a new Book object.
     *
     * @param title title of book, may be null
     * @param author author of book, may be null
     * @param publicationYear the first publication year of book, may be null
     * @param isbn the isbn of the book, may be null
     * @param genre the genre of the book, may be null
     * @param coverID the coverID of the book, may be null
     * @return a new Book
     */

    public Book create( String title, String author,
    Integer publicationYear, String isbn, String genre,
    Integer coverID){
    if (isbn == null | isbn.isBlank())throw new IllegalArgumentException("Book must have isbn");

    return new Book(
            title,
            author,
            publicationYear,
            isbn,
            genre,
            coverID);
    }



}
