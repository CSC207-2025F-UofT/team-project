package Entities;

/** Represents an item listed by the eBay Browse API. */
public class Item {
  private final String itemId;
  private final String title;
  private final double price;
  private final String currency;
  private final String condition;
  private final String category;
  private final Double sellerFeedbackPercent;

  /** Construct an item, given all of its fields as an input. */
  public Item(
      String itemId,
      String title,
      double price,
      String currency,
      String condition,
      String category,
      Double sellerFeedbackPercent) {
    this.itemId = itemId;
    this.title = title;
    this.price = price;
    this.currency = currency;
    this.condition = condition;
    this.category = category;
    this.sellerFeedbackPercent = sellerFeedbackPercent;
  }

  public String getItemId() {
    return itemId;
  }

  public String getTitle() {
    return title;
  }

  public double getPrice() {
    return price;
  }

  public String getCurrency() {
    return currency;
  }

  public String getCondition() {
    return condition;
  }

  public String getCategory() {
    return category;
  }

  public Double getSellerFeedbackPercent() {
    return sellerFeedbackPercent;
  }
}
