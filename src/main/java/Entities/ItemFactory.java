package Entities;

import java.math.BigDecimal;

/**
 * Uses the factory pattern to build items directly from strings, which is much less painful than
 * constantly converting API output in the DAO, since the API could return null values. Validates
 * and normalizes ID, Name, Price and Currency.
 */
public class ItemFactory {

  private static final String UNKNOWN_CONDITION = "Unknown";
  private static final String UNKNOWN_CATEGORY = "Uncategorised";

  /**
   * Creates an item using string representations of each field. Best used in the DataAccessObject.
   *
   * @throws IllegalArgumentException if required data is missing or invalid
   */
  public Item create(
      String itemId,
      String title,
      String priceValue,
      String currency,
      String condition,
      String category,
      Double sellerFeedbackPercent) {
    if (isBlank(itemId) || isBlank(title) || isBlank(priceValue) || isBlank(currency)) {
      throw new IllegalArgumentException("Item needs an id, title, price and currency");
    }
    Double price = new Double(priceValue.trim());
    if (price < 0) {
      throw new IllegalArgumentException("Price must not be negative: " + priceValue);
    }
    BigDecimal shipping = null;
    return new Item(
        itemId.trim(),
        title.trim(),
        price,
        currency.trim().toUpperCase(),
        isBlank(condition) ? UNKNOWN_CONDITION : condition.trim(),
        isBlank(category) ? UNKNOWN_CATEGORY : category.trim(),
        sellerFeedbackPercent);
  }

  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }
}
