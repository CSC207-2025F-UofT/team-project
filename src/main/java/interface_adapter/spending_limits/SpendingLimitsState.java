package interface_adapter.spending_limits;

import java.util.HashMap;
import java.util.Map;

public class SpendingLimitsState {

    private Map<String, Double> limits;
    private String username;

    public SpendingLimitsState() {
        this.limits = new HashMap<>();
    }

    public Map<String, Double> getLimits() {
        return limits;
    }

    public void setLimits(Map<String, Double> limits) {
        this.limits = limits;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
