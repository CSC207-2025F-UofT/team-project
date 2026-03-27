package interface_adapter.upload_statement;

public class    UploadStatementState {
    private double totalSpend;
    private String username;

    public double getTotalSpend() {
        return totalSpend;
    }
    public void setTotalSpend(double totalSpend) { this.totalSpend = totalSpend; }
    public String getUsername() {return this.username;}
    public void setUsername(String username) { this.username = username; }
}
