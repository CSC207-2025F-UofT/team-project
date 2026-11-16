package use_case.google_login;

import java.io.IOException;

/** Authenticates a Google user and returns their stable provider subject. */
public interface GoogleIdentityGateway {
    String authenticate() throws IOException, InterruptedException;
}
