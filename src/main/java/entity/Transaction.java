package entity;

public class Transaction {
    private final int id;
    private final int userId;
    private final String date;
    private final String merchant;
    private final float amount;
    private final String category;

    public Transaction(int id, int userId, String date, String merchant, float amount, String category) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.merchant = merchant;
        this.amount = amount;
        this.category = category;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public String getMerchant() { return merchant; }
    public float getAmount() { return amount; }
    public String getCategory() { return category; }
}
