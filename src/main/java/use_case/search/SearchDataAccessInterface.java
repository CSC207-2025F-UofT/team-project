package use_case.search;

import java.util.List;

/**
 * DAO interface for the Search Use Case.
 */
public interface SearchDataAccessInterface {
    /**
     * Search with query and return the result as a List of Book.
     * @param query the query to search for
     * @return String of the list of books
     */
    String search(String query);
}
