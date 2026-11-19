package use_case.generate_shopping_list;

import entity.PantryItem;
import entity.ShoppingItem;

import java.time.LocalDate;

/**
 * Decides whether an ingredient should appear on the shopping list and why.
 * Strategy pattern: swap the policy without changing the generate interactor.
 */
public interface ShoppingNeedPolicy {

    /**
     * Classifies a pantry item, or returns null if the ingredient can be skipped.
     *
     * @param pantryItem the matching pantry item, or null if not in the fridge
     * @param today today's date
     * @return MISSING, LOW, or null to skip
     */
    ShoppingItem.Reason classify(PantryItem pantryItem, LocalDate today);
}
