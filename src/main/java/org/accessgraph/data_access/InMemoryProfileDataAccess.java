package org.accessgraph.data_access;

import org.accessgraph.entity.AccessibilityProfile;
import org.accessgraph.use_case.plan_route.ProfileDataAccessInterface;

/** Temporary profile store for the MVP. User Story 2 replaces it with a saved profile. */
public final class InMemoryProfileDataAccess implements ProfileDataAccessInterface {
    private AccessibilityProfile profile;

    public InMemoryProfileDataAccess(AccessibilityProfile profile) {
        this.profile = profile;
    }

    @Override
    public AccessibilityProfile getProfile() {
        return profile;
    }

    public void setProfile(AccessibilityProfile profile) {
        this.profile = profile;
    }
}
