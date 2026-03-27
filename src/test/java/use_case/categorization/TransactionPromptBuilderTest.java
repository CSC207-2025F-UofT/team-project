package use_case.categorization;

import entity.Transaction;
import org.junit.jupiter.api.Test;
import use_case.transaction_categorizer.TransactionPromptBuilder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionPromptBuilderTest {

    @Test
    void buildPromptContainsDescriptionAmountAndCategories() {
        Transaction t = new Transaction(
                LocalDate.of(2025, 11, 1),
                "Uncategorized",
                45.50,
                "Grocery store"
        );

        String prompt = TransactionPromptBuilder.buildPrompt(t);

        assertTrue(prompt.contains("Grocery store"));

        String upperPrompt = prompt.toUpperCase();
        assertTrue(upperPrompt.contains("SHOPPING"));
        assertTrue(upperPrompt.contains("DINING OUT"));
        assertTrue(upperPrompt.contains("ENTERTAINMENT"));
    }

    @Test
    void isValidRecognizesKnownCategoriesCaseInsensitive() {
        assertTrue(TransactionPromptBuilder.isValid("shopping"));
        assertTrue(TransactionPromptBuilder.isValid("DINING OUT"));
        assertTrue(TransactionPromptBuilder.isValid("utilities"));
    }

    @Test
    void isValidRejectsUnknownCategories() {
        assertFalse(TransactionPromptBuilder.isValid("FOOD"));
        assertFalse(TransactionPromptBuilder.isValid("RENT"));
        assertFalse(TransactionPromptBuilder.isValid("")); // empty
    }
}


