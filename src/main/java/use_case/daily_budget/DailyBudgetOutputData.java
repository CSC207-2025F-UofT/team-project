package use_case.daily_budget;

import entity.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/** Budget summary for one day, with all amounts in the trip's home currency. */
public final class DailyBudgetOutputData {
  private final LocalDate date;
  private final String currency;
  private final BigDecimal dailyBudget;
  private final BigDecimal carryOver;
  private final BigDecimal spent;
  private final Map<ExpenseCategory, BigDecimal> categoryTotals;
  private final BigDecimal projectedCost;
  private final BigDecimal totalBudget;

  public DailyBudgetOutputData(
      LocalDate date,
      String currency,
      BigDecimal dailyBudget,
      BigDecimal carryOver,
      BigDecimal spent,
      Map<ExpenseCategory, BigDecimal> categoryTotals,
      BigDecimal projectedCost,
      BigDecimal totalBudget) {
    this.date = date;
    this.currency = currency;
    this.dailyBudget = dailyBudget;
    this.carryOver = carryOver;
    this.spent = spent;
    final Map<ExpenseCategory, BigDecimal> categories = new EnumMap<>(ExpenseCategory.class);
    categories.putAll(categoryTotals);
    this.categoryTotals = Collections.unmodifiableMap(categories);
    this.projectedCost = projectedCost;
    this.totalBudget = totalBudget;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getCurrency() {
    return currency;
  }

  public BigDecimal getDailyBudget() {
    return dailyBudget;
  }

  public BigDecimal getCarryOver() {
    return carryOver;
  }

  public BigDecimal getAllowance() {
    return dailyBudget.add(carryOver);
  }

  public BigDecimal getSpent() {
    return spent;
  }

  public BigDecimal getRemaining() {
    return getAllowance().subtract(spent);
  }

  public BigDecimal getDayOverage() {
    return getRemaining().negate().max(BigDecimal.ZERO);
  }

  public Map<ExpenseCategory, BigDecimal> getCategoryTotals() {
    return categoryTotals;
  }

  public BigDecimal getProjectedCost() {
    return projectedCost;
  }

  public BigDecimal getTotalBudget() {
    return totalBudget;
  }

  public BigDecimal getProjectedOverage() {
    return projectedCost.subtract(totalBudget).max(BigDecimal.ZERO);
  }
}
