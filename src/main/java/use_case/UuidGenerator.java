package use_case;

import java.util.UUID;

public class UuidGenerator implements IdGeneratorInterface {
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
