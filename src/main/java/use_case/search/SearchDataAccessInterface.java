package use_case.search;

import java.util.List;

public interface SearchDataAccessInterface {
    List<String> search(String query);
}
