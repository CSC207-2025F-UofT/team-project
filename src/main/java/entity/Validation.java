package entity;

import java.util.Currency;

/**
 * Shared argument checks for entity constructors and setters. Keeping them in one place means every
 * entity enforces the same rules.
 */
final class Validation {

  private Validation() {}

  /**
   * Returns the value with surrounding whitespace removed.
   *
   * @param value the text to check
   * @param fieldName the name used in the error message
   * @return the trimmed value
   * @throws IllegalArgumentException if the value is null or blank
   */
  static String requireNonBlank(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " cannot be empty");
    }
    return value.trim();
  }

  /**
   * Checks that an amount is a finite number greater than zero.
   *
   * @param amount the amount to check
   * @param fieldName the name used in the error message
   * @return the amount
   * @throws IllegalArgumentException if the amount is not positive
   */
  static double requirePositive(double amount, String fieldName) {
    if (!Double.isFinite(amount) || amount <= 0) {
      throw new IllegalArgumentException(fieldName + " must be greater than 0");
    }
    return amount;
  }

  /**
   * Checks that an amount is a finite number that is zero or more.
   *
   * @param amount the amount to check
   * @param fieldName the name used in the error message
   * @return the amount
   * @throws IllegalArgumentException if the amount is negative
   */
  static double requireNonNegative(double amount, String fieldName) {
    if (!Double.isFinite(amount) || amount < 0) {
      throw new IllegalArgumentException(fieldName + " cannot be negative");
    }
    return amount;
  }

  /**
   * Checks that a value is not null.
   *
   * @param value the value to check
   * @param fieldName the name used in the error message
   * @param <T> the type of the value
   * @return the value
   * @throws IllegalArgumentException if the value is null
   */
  static <T> T requireNonNull(T value, String fieldName) {
    if (value == null) {
      throw new IllegalArgumentException(fieldName + " is required");
    }
    return value;
  }

  /**
   * Returns the upper-case ISO 4217 code (for example "CAD").
   *
   * @param code the currency code to check
   * @return the normalized code
   * @throws IllegalArgumentException if the code is not a known ISO 4217 currency
   */
  static String requireCurrencyCode(String code) {
    final String normalized = requireNonBlank(code, "Currency").toUpperCase();
    try {
      return Currency.getInstance(normalized).getCurrencyCode();
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Unknown currency: " + code, ex);
    }
  }
}
