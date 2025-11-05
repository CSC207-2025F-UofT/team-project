package dataaccessinterface;

import entity.BookmarkedLocation;
import java.util.List;

public interface BookmarkedLocationStorage {
    // Get the bookmarked location.
    List<BookmarkedLocation> getBookmarkedLocation();

    // Add the bookmarked location.
    void addBookmarkedLocation(BookmarkedLocation bookmarkedLocation);

}
