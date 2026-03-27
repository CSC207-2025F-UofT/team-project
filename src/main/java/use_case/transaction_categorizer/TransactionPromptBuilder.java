package use_case.transaction_categorizer;

import entity.Transaction;

import java.util.List;

public class TransactionPromptBuilder {

    static final String FIXED_CATEGORIES =
            "SHOPPING, DINING OUT, ENTERTAINMENT, UTILITIES, MISCELLANEOUS, FITNESS, TRANSPORTATION";

    public static String buildPrompt(Transaction t) {
        return """
            You are a bank transaction classifier.

            Choose EXACTLY ONE of these categories for the transaction:
            %s

            Respond with ONLY the category name. No explanation, no extra words.

            Transaction:
            Description: %s
            Amount: $%.2f
            """
                .formatted(
                        FIXED_CATEGORIES,
                        t.getMerchant(),
                        t.getAmount()
                );
    }

    public static String buildBatchPrompt(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder("""
            You are a bank transaction classifier.

            Categorize EACH transaction into EXACTLY ONE of these categories:

            SHOPPING, DINING OUT, ENTERTAINMENT, UTILITIES, MISCELLANEOUS, FITNESS, TRANSPORTATION

            Respond ONLY in this exact JSON format:

            {
              "results": [
                {"description": "...", "category": "..."},
                ...
              ]
            }

            Transactions:
            """);

        for (Transaction t : transactions) {
            sb.append("- ")
                    .append(t.getMerchant())
                    .append(" $")
                    .append(t.getAmount())
                    .append("\n");
        }

        return sb.toString();
    }

    public static boolean isValid(String category) {
        if (category == null || category.trim().isEmpty()) {
            return false;
        }
        return FIXED_CATEGORIES
                .toUpperCase()
                .contains(category.toUpperCase());

    }
}
