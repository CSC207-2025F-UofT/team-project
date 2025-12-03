package use_case.transaction_categorizer;

import entity.Transaction;
import use_case.ai_client.GeminiClient;

import java.util.List;
import java.util.Arrays;

public class TransactionCategorizerService {

    private final GeminiClient gemini;
    private static final List<String> ALLOWED_CATEGORIES = Arrays.asList(
            "groceries", "dining out", "entertainment", "utilities", "fitness", "transportation", "miscellaneous"
    );

    public TransactionCategorizerService(GeminiClient gemini) {
        this.gemini = gemini;
    }

    public void categorizeTransactions(List<Transaction> transactions) {
        for (Transaction t : transactions) {
            String prompt = TransactionPromptBuilder.buildPrompt(t);
            String category = gemini.generateInsight(prompt).trim().toLowerCase();

            if (!ALLOWED_CATEGORIES.contains(category)) {
                category = "miscellaneous";
            }

            t.setCategory(category);
        }
    }
}
