package entity;

import java.math.BigDecimal;

/**
 * An entity representing a user's daily spending limit.
 */
public class Budget {
    private final BigDecimal dailyLimit;
    
    /**
     * Creates a new budget with the given daily limit.
     * @param dailyLimit the daily spending limit
     * @throws IllegalArgumentException if the limit is null or not greater than zero
     */
    public Budget(BigDecimal dailyLimit) {
       if (dailyLimit == null || dailyLimit.compareTo(BigDecimal.ZERO) <= 0) {
           throw new IllegalArgumentException("Limit must be greater than zero");
       }
       
       this.dailyLimit = dailyLimit;
    }

    public BigDecimal getLimit() {
        return dailyLimit;
    }
}
