package use_case.create_plan;

import java.util.UUID;

public record CreatePlanOutputData(UUID planId, String name, int exerciseCount) {
}
