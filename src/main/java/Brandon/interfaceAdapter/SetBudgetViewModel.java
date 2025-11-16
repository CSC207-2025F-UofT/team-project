package Brandon.interfaceAdapter;

public class SetBudgetViewModel {
    private String month;
    private float limit;
    private boolean success;
    private String message;

    public String getMonth() {return month;}
    public float getLimit() {return limit;}
    public boolean isSuccess() {return success;}
    public String getMessage() {return message;}

    public void setMonth(String month) {this.month = month;}
    public void setLimit(float limit) {this.limit = limit;}
    public void setSuccess(boolean success) {this.success = success;}
    public void setMessage(String message) {this.message = message;}


}
