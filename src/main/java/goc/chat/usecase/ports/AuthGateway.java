package goc.chat.usecase.ports;

public interface AuthGateway {
    boolean validatePassword(String input, String storedHash);
    String hashPassword(String plainText);
}
