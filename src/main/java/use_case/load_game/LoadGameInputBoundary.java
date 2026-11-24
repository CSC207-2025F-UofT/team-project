package use_case.load_game;

import java.util.UUID;

public interface LoadGameInputBoundary {
    void execute(String username, UUID runId);
}
