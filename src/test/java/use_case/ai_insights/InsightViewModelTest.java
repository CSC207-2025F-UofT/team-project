package use_case.ai_insights;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsightViewModelTest {

    @Test
    void settersAndGettersWork() {
        InsightViewModel vm = new InsightViewModel();

        vm.setSummary("test summary");
        vm.setRecommendations("tip 1");
        vm.setDate("2025-01-01");
        vm.setSpendingBreakdown("Food: $50");

        assertEquals("test summary", vm.getSummary());
        assertEquals("tip 1", vm.getRecommendations());
        assertEquals("2025-01-01", vm.getDate());
        assertEquals("Food: $50", vm.getSpendingBreakdown());
    }
}
