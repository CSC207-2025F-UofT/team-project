package entity;

import java.math.BigDecimal;

public class Budget {
    private BigDecimal dailyLimit;
    
    public Budget(String username, BigDecimal dailyLimit) {
       if (dailyLimit.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Limit must be greater than zero");
       }
       
       this.dailyLimit = dailyLimit;
    }

    public BigDecimal getLimit() {
        return dailyLimit;
    }
}
