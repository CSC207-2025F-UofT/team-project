package org.accessgraph.use_case.plan_route;

import org.accessgraph.entity.AccessibilityProfile;

/** Supplies the passenger's current accessibility profile. Implemented in the data access layer. */
public interface ProfileDataAccessInterface {
    AccessibilityProfile getProfile();
}
