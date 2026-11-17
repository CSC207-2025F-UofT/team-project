package entity;
import java.time.LocalDateTime;

/**
 * Represents a single book.
 */
public class Book {
    private final String openLibraryId;
    private final String title;
    private final String author;
    private final String isbn;
    private final String genre;
    private final Integer publicationYear;
    private final Integer coverID;
    private final LocalDateTime dateAdded;

    public Book(String openLibraryId, String title, String author,
                 Integer publicationYear, String isbn, String genre,
                 Integer coverID, LocalDateTime dateAdded){
        this.openLibraryId = openLibraryId;
        this.title =title;
        this.author =author;
        this.isbn =isbn;
        this.genre =genre;
        this.publicationYear =publicationYear;
        this.coverID = coverID;
        this.dateAdded = dateAdded;
    }

    /**
     * Returns the OpenLibraryID of the Book.
     * @return the OpenLibraryID of the Book.
     */
    public String getOpenLibraryId(){
        return openLibraryId;
    }

    /**
     * Returns the title of the Book.
     * @return the title of the Book.
     */
    public String getTitle(){
        return title;
    }

    /**
     * Returns the author of the Book.
     * @return the author of the Book.
     */
    public String getAuthor(){
        return author;
    }

    /**
     * Returns the isbn of the Book.
     * @return the isbn of the Book.
     */
    public String getIsbn(){
        return isbn;
    }

    /**
     * Returns the publication year of the Book.
     * @return the publication year of the Book.
     */
    public Integer getPublicationYear(){
        return publicationYear;
    }

    public String getGenre(){
        return genre;
    }
    public LocalDateTime getDateAdded(){
        return dateAdded;
    }
    @Override
    public String toString(){
        return title + "by" + author;
    }
}



