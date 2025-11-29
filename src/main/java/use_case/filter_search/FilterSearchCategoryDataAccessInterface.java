package use_case.filter_search;

import java.util.List;

public interface FilterSearchCategoryDataAccessInterface {
    List<String> getAllCategories();
    boolean categoryExists(String category);
}
