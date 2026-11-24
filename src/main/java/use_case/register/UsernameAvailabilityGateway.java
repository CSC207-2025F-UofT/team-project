package use_case.register;

public interface UsernameAvailabilityGateway {
    /** Returns true only if another account is confirmed to hold this username. */
    boolean isTaken(String username);
}
