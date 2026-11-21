package data_access;

import entity.Book;
import entity.Review;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * File-based DAO for books the user has added, including their reviews.
 * Books are stored as JSON in a file, keyed by OpenLibraryID, so they persist between app runs.
 * Books that were only searched stay in {@link InMemoryBookDataAccessObject}.
 * The bookshelf reads and writes books through {@link FileBookshelfDataAccessObject}.
 */
public class FileBookDataAccessObject {
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
    private final Map<String, Book> books = new LinkedHashMap<>();

    /**
     * Constructs this DAO and loads any books already saved in the file.
     * If the file does not exist, it starts empty and the file is created on the first save.
     * @param filePath the path of the JSON file to read from and write to
     * @throws IOException if the file exists but cannot be read
     */
    public FileBookDataAccessObject(String filePath) throws IOException {
        this.filePath = Paths.get(filePath);
        if (Files.exists(this.filePath)) {
            load();
        }
    }

    /**
     * Saves a book, replacing any existing book with the same OpenLibraryID,
     * and writes all books to the file.
     * @param book the book to save
     */
    public void save(Book book) {
        books.put(book.getOpenLibraryId(), book);
        writeToFile();
    }

    /**
     * Replaces all saved books with the given books and writes them to the file once.
     * If two books share an OpenLibraryID, the later one is kept.
     * @param newBooks the books to save, in the order they should be stored
     */
    public void saveAll(List<Book> newBooks) {
        books.clear();
        for (Book book : newBooks) {
            books.put(book.getOpenLibraryId(), book);
        }
        writeToFile();
    }

    /**
     * Returns the saved book with the given OpenLibraryID.
     * @param openLibraryId the OpenLibraryID of the book
     * @return the book, or null if no book with that OpenLibraryID is saved
     */
    public Book getBook(String openLibraryId) {
        return books.get(openLibraryId);
    }

    /**
     * Checks whether a book with the given OpenLibraryID is saved.
     * @param openLibraryId the OpenLibraryID of the book
     * @return true if the book is saved, false otherwise
     */
    public boolean existsByOpenLibraryId(String openLibraryId) {
        return books.containsKey(openLibraryId);
    }

    /**
     * Removes the book with the given OpenLibraryID, if it is saved, and updates the file.
     * @param openLibraryId the OpenLibraryID of the book to remove
     */
    public void remove(String openLibraryId) {
        if (books.remove(openLibraryId) != null) {
            writeToFile();
        }
    }

    /**
     * Returns all saved books in the order they were added.
     * @return a list of all saved books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    /**
     * Reads all books from the file into memory.
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
            books.put(book.getOpenLibraryId(), book);
        }
    }

    /**
     * Writes all books in memory to the file, overwriting its previous contents.
     * @throws RuntimeException if the file cannot be written
     */
    private void writeToFile() {
        JSONArray array = new JSONArray();
        for (Book book : books.values()) {
            array.put(toJson(book));
        }
        try {
            Files.writeString(filePath, array.toString(2), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not write books to " + filePath, e);
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