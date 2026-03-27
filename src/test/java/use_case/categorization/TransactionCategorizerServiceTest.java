package use_case.categorization;

import entity.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import use_case.transaction_categorizer.GeminiClient;
import use_case.transaction_categorizer.TransactionCategorizerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCategorizerServiceTest {

    static class StubGeminiClient extends GeminiClient {
        private final String response;

        StubGeminiClient(String response) {
            this.response = response;
        }

        @Override
        public String askGemini(String prompt) {
            return response;
        }
    }

    @Test
    void categorizeAssignsCategoriesFromBatchJson() {
        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction(LocalDate.of(2025, 11, 1), "Uncategorized", 45.50, "Grocery store"));
        txs.add(new Transaction(LocalDate.of(2025, 11, 5), "Uncategorized", 35.00, "Gas station"));

        JSONArray arr = new JSONArray();
        arr.put(new JSONObject()
                .put("description", "Grocery store")
                .put("category", "Shopping"));
        arr.put(new JSONObject()
                .put("description", "Gas station")
                .put("category", "Transportation"));

        String stubResponse = arr.toString();

        TransactionCategorizerService service =
                new TransactionCategorizerService(new StubGeminiClient(stubResponse));

        service.categorize(txs);

        assertEquals("SHOPPING", txs.get(0).getCategory());
        assertEquals("TRANSPORTATION", txs.get(1).getCategory());
    }

    @Test
    void categorizeFallsBackToMiscWhenInvalidCategory() {
        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction(LocalDate.of(2025, 11, 10), "Uncategorized", 29.99, "Movie tickets"));

        JSONArray arr = new JSONArray();
        arr.put(new JSONObject()
                .put("description", "Movie tickets")
                .put("category", "FunStuff")); // invalid

        String stubResponse = arr.toString();

        TransactionCategorizerService service =
                new TransactionCategorizerService(new StubGeminiClient(stubResponse));

        service.categorize(txs);

        assertEquals("MISCELLANEOUS", txs.get(0).getCategory());
    }

    @Test
    void categorizeHandlesErrorResponseAsMisc() {
        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction(LocalDate.of(2025, 11, 1), "Uncategorized", 10.00, "Test"));

        // Simulate quota error / bad JSON
        String errorJson = """
                {
                  "error": {
                    "code": 429,
                    "message": "Quota exceeded"
                  }
                }
                """;

        TransactionCategorizerService service =
                new TransactionCategorizerService(new StubGeminiClient(errorJson));

        service.categorize(txs);

        assertEquals("MISCELLANEOUS", txs.get(0).getCategory());
    }

    @Test
    void categorizeHandlesMissingApiKeyAsErrNoKey() {
        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction(LocalDate.of(2025, 11, 1), "Uncategorized", 10.00, "Test"));

        TransactionCategorizerService service =
                new TransactionCategorizerService(new StubGeminiClient("MISCELLANEOUS"));

        service.categorize(txs);

        assertEquals("MISCELLANEOUS", txs.get(0).getCategory());
    }
}

