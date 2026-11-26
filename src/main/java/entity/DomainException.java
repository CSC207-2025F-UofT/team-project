package entity;

import java.util.Objects;

/** A failed domain query or precondition; never contains presentation objects. */
public final class DomainException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final ResultCode code;

    public DomainException(ResultCode code, String message) {
        super(Objects.requireNonNull(message, "message"));
        this.code = Objects.requireNonNull(code, "code");
    }

    public ResultCode code() { return code; }
}
