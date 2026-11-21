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
    private final Integer publicationYear;
    private final Integer coverID;
    private LocalDateTime dateAdded;
    private boolean shelved;
    private Review review;


    public Book(String openLibraryId, String title, String author,
                 Integer publicationYear, String isbn,
                Integer coverID){
        this.openLibraryId = openLibraryId;
        this.title =title;
        this.author =author;
        this.isbn =isbn;
        this.publicationYear =publicationYear;
        this.coverID = coverID;
        this.dateAdded = null;
        this.shelved = false;
        this.review = null;
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


    /**
     * Returns the coverID of the Book.
     * @return the coverID of the Book.
     */
    public Integer getCoverID() {
        return coverID;
    }

    /**
     * Returns the date added of the book to the bookshelf.
     * @return the date added of the book to the bookshelf.
     */
    public LocalDateTime getDateAdded(){
        return dateAdded;
    }


    /**
     * Returns if book is added to bookshelf, true if added, false if not.
     * @return if book is added to bookshelf, true if added, false if not.
     */
    public boolean isShelved() {
        return shelved;
    }

    /*
     * Sets whether book is added to bookshelf.
     * Sets the date added for the book to the bookshelf.
     * @param shelved true to shelve book, false to unshelve book.
     */
    public void setShelved(boolean shelved){
        if (this.shelved && shelved ){
            this.dateAdded = LocalDateTime.now();
        } else if (!shelved) {
            this.dateAdded = null;
        }
        this.shelved = shelved;
    }

    /**
     * Returns the review of the Book.
     * @return the review of the Book.
     */
    public Review getReview(){
        return review;
    }

    /**
     * Sets the review of the Book.
     * @param review from Review class.
     */
    public void setReview(Review review){
        this.review = review;
    }

    @Override
    public String toString(){
        return "Book{" +
        "openLibraryId: " + openLibraryId +
        ", title:" + title +
        " author:" + author +
        ", isbn:" + isbn +
        ", publicationYear:" + publicationYear +
        ", coverID:" + coverID +
        ", dateAdded:" + dateAdded +
        ", shelved: " + shelved +
        ", review: " + review + "}";
    }

    /**
     * Two books are equal if they share the same openLibraryId.
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (!(obj instanceof Book)){
            return false;
        }
        return openLibraryId.equals(((Book) obj).getOpenLibraryId());
    }


}



