package use_case.transaction_categorizer;

import entity.Transaction;

public class TransactionPromptBuilder {

    private static final String ALLOWED_CATEGORIES = "groceries, dining out, entertainment, utilities, fitness, transportation, miscellaneous";

    public static String buildPrompt(Transaction transaction) {
        return "Please categorize this transaction into ONE of the following categories: "
                + ALLOWED_CATEGORIES + ".\n"
                + "Description: " + transaction.getDescription() + "\n"
                + "Amount: $" + transaction.getAmount();
    }
}
