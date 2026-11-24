package use_case.upload_receipt;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class UploadReceiptOutputData {
    private final String merchant;
    private final BigDecimal total;
    private final LocalDate date;
    private final String storedReceiptReference;

    public UploadReceiptOutputData(String merchant, BigDecimal total,
                                   LocalDate date, String storedReceiptReference) {
        this.merchant = merchant;
        this.total = total;
        this.date = date;
        this.storedReceiptReference = storedReceiptReference;
    }
    public String getMerchant() { return merchant; }
    public BigDecimal getTotal() { return total; }
    public LocalDate getDate() { return date; }
    public String getStoredReceiptReference() { return storedReceiptReference; }
}
