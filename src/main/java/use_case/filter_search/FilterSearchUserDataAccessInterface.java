package use_case.filter_search;

import entity.Transaction;
import java.time.LocalDate;
import java.util.List;

public interface FilterSearchUserDataAccessInterface {

    List<Transaction> search(String searchText,
                             String category,
                             LocalDate startDate,
                             LocalDate endDate,
                             Double minAmount,
                             Double maxAmount);
}
