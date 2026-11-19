package org.accessgraph.use_case.sign_in;

@FunctionalInterface
public interface Authenticator {
    boolean matches(String username, char[] password);
}
