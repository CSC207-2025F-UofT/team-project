package interface_adapter.upload_receipt;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class UploadReceiptViewModel {
    private String merchant = "";
    private BigDecimal total = BigDecimal.ZERO;
    private LocalDate date;
    private String receiptReference = "";
    private String message = "";
    private boolean error;

    public String getMerchant() { return merchant; }
    public BigDecimal getTotal() { return total; }
    public LocalDate getDate() { return date; }
    public String getReceiptReference() { return receiptReference; }
    public String getMessage() { return message; }
    public boolean isError() { return error; }

    void setSuccess(String merchant, BigDecimal total, LocalDate date, String reference) {
        this.merchant = merchant; this.total = total; this.date = date;
        receiptReference = reference; message = "Receipt uploaded. Review the extracted details.";
        error = false;
    }
    void setFailure(String value) { message = value; error = true; }
}
