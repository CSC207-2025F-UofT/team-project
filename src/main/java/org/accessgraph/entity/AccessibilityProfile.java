package org.accessgraph.entity;

/**
 * A passenger's routing requirements. Minimal version for the route-planning MVP;
 * the accessibility-profile use case (User Story 2) will extend and persist it.
 */
public final class AccessibilityProfile {
    private final boolean stepFreeRequired;
    private final int maxTransfers;

    public AccessibilityProfile(boolean stepFreeRequired, int maxTransfers) {
        if (maxTransfers < 0) {
            throw new IllegalArgumentException("Maximum transfers cannot be negative.");
        }
        this.stepFreeRequired = stepFreeRequired;
        this.maxTransfers = maxTransfers;
    }

    /** True when the passenger can only board, transfer and exit at confirmed accessible stations. */
    public boolean isStepFreeRequired() {
        return stepFreeRequired;
    }

    public int getMaxTransfers() {
        return maxTransfers;
    }

    /** Whether the passenger may board, exit or change lines at this station. */
    public boolean canUse(Station station) {
        return !stepFreeRequired || station.getAccessibility() == Station.Accessibility.CONFIRMED;
    }
}
