package use_case.ports;

public interface AuthGateway {
    boolean validatePassword(String input, String storedHash);
    String hashPassword(String plainText);
}
