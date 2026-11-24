package use_case;

import java.util.UUID;

public interface ManageSavesInputBoundary {
    void list(String username);

    void delete(String username, UUID runId);
}
