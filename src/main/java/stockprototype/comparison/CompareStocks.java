package stockprototype.comparison;

import java.io.IOException;

public interface CompareStocks {
    /** Compare up to maxSessions recent shared closes. Zero selects the entire shared history. */
    ComparisonResult compare(String first, String second, int maxSessions) throws IOException;
}
