package data_access;

import entity.Book;
import entity.Bookshelf;
import entity.Review;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.addBook.BookshelfDataAccessInterface;
import use_case.removeBook.RemoveBookDataAccessInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * File-based DAO for the user's bookshelf.
 * The bookshelf is loaded from a JSON file once, when this DAO is created,
 * and the whole bookshelf is written back to the file every time it changes,
 * so the saved books (including their reviews) persist between app runs.
 * Books are identified by their OpenLibraryID.
 * Books that were only searched stay in {@link InMemoryBookDataAccessObject}.
 */
public class FileBookshelfDataAccessObject
        implements BookshelfDataAccessInterface, RemoveBookDataAccessInterface {
    private static final String OPEN_LIBRARY_ID = "openLibraryId";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String PUBLICATION_YEAR = "publicationYear";
    private static final String ISBN = "isbn";
    private static final String COVER_ID = "coverID";
    private static final String SHELVED = "shelved";
    private static final String DATE_ADDED = "dateAdded";
    private static final String RATING = "rating";
    private static final String REVIEW = "review";

    private final Path filePath;
    private Bookshelf bookshelf = new Bookshelf();

    /**
     * Constructs this DAO and loads the bookshelf saved in the file.
     * If the file does not exist, the bookshelf starts empty and the file is created on the first save.
     * @param filePath the path of the JSON file to read from and write to
     * @throws IOException if the file exists but cannot be read
     */
    public FileBookshelfDataAccessObject(String filePath) throws IOException {
        this.filePath = Paths.get(filePath);
        if (Files.exists(this.filePath)) {
            load();
        }
    }

    /**
     * Returns the user's bookshelf.
     * @return the bookshelf, containing every saved book in the order they were added
     */
    @Override
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * Checks whether a book with the same OpenLibraryID is on the bookshelf.
     * @param book the book to look for
     * @return true if the book is on the bookshelf, false otherwise
     */
    @Override
    public boolean existsOnBookshelf(Book book) {
        return book != null && getBook(book.getOpenLibraryId()) != null;
    }

    /**
     * Saves the bookshelf and writes it to the file.
     * Books that were not shelved yet are marked as shelved, which sets their date added.
     * @param bookshelf the bookshelf to save
     */
    @Override
    public void saveBookshelf(Bookshelf bookshelf) {
        for (Book book : bookshelf.getBooks()) {
            if (!book.isShelved()) {
                book.setShelved(true);
            }
        }
        this.bookshelf = bookshelf;
        writeToFile();
    }

    /**
     * Returns the book on the bookshelf with the given OpenLibraryID.
     * @param openLibraryId the OpenLibraryID of the book
     * @return the book, or null if it is not on the bookshelf
     */
    @Override
    public Book getBook(String openLibraryId) {
        for (Book book : bookshelf.getBooks()) {
            if (book.getOpenLibraryId().equals(openLibraryId)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Reads the bookshelf from the file.
     * @throws IOException if the file cannot be read
     */
    private void load() throws IOException {
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        if (content.isBlank()) {
            return;
        }
        JSONArray array = new JSONArray(content);
        for (int i = 0; i < array.length(); i++) {
            Book book = fromJson(array.getJSONObject(i));
            if (getBook(book.getOpenLibraryId()) == null) {
                bookshelf.addBook(book);
            }
        }
    }

    /**
     * Writes every book on the bookshelf to the file, overwriting its previous contents.
     * @throws RuntimeException if the file cannot be written
     */
    private void writeToFile() {
        JSONArray array = new JSONArray();
        for (Book book : bookshelf.getBooks()) {
            array.put(toJson(book));
        }
        try {
            Files.writeString(filePath, array.toString(2), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not write bookshelf to " + filePath, e);
        }
    }

    /**
     * Converts a book to a JSON object. Null fields are stored as JSON null,
     * since org.json drops keys whose value is Java null.
     * @param book the book to convert
     * @return the JSON representation of the book
     */
    private JSONObject toJson(Book book) {
        JSONObject json = new JSONObject();
        json.put(OPEN_LIBRARY_ID, book.getOpenLibraryId());
        json.put(TITLE, orNull(book.getTitle()));
        json.put(AUTHOR, orNull(book.getAuthor()));
        json.put(PUBLICATION_YEAR, orNull(book.getPublicationYear()));
        json.put(ISBN, orNull(book.getIsbn()));
        json.put(COVER_ID, orNull(book.getCoverID()));
        json.put(SHELVED, book.isShelved());
        json.put(DATE_ADDED, book.getDateAdded() == null
                ? JSONObject.NULL : book.getDateAdded().toString());

        Review review = book.getReview();
        json.put(RATING, review == null ? JSONObject.NULL : review.getRating());
        json.put(REVIEW, review == null ? JSONObject.NULL : orNull(review.getReview()));
        return json;
    }

    /**
     * Converts a JSON object back into a book.
     * Missing or null fields become null, except shelved, which defaults to false.
     * OpenLibraryID is required.
     * A review is only created if a rating was saved, since every review has a rating.
     * @param json the JSON object to convert
     * @return the book represented by the JSON object
     */
    private Book fromJson(JSONObject json) {
        String dateAdded = stringOrNull(json, DATE_ADDED);

        Book book = new Book(
                json.getString(OPEN_LIBRARY_ID),
                stringOrNull(json, TITLE),
                stringOrNull(json, AUTHOR),
                intOrNull(json, PUBLICATION_YEAR),
                stringOrNull(json, ISBN),
                intOrNull(json, COVER_ID)
        );
        // setShelved(true) stamps the current time, so restore the saved date afterwards.
        book.setShelved(json.optBoolean(SHELVED, false));
        book.setDateAdded(dateAdded == null ? null : LocalDateTime.parse(dateAdded));

        Integer rating = intOrNull(json, RATING);
        if (rating != null) {
            book.setReview(new Review(book.getOpenLibraryId(), rating, stringOrNull(json, REVIEW)));
        }
        return book;
    }

    /**
     * Returns the value, or JSONObject.NULL if the value is null.
     * @param value the value to store
     * @return the value or JSONObject.NULL
     */
    private static Object orNull(Object value) {
        return value == null ? JSONObject.NULL : value;
    }

    /**
     * Returns the string stored under the key, or null if it is missing or JSON null.
     * @param json the JSON object to read from
     * @param key the key to read
     * @return the string, or null
     */
    private static String stringOrNull(JSONObject json, String key) {
        return json.isNull(key) ? null : json.getString(key);
    }

    /**
     * Returns the integer stored under the key, or null if it is missing or JSON null.
     * @param json the JSON object to read from
     * @param key the key to read
     * @return the integer, or null
     */
    private static Integer intOrNull(JSONObject json, String key) {
        return json.isNull(key) ? null : json.getInt(key);
    }
}
