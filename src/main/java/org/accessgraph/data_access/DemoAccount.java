package org.accessgraph.data_access;

import org.accessgraph.use_case.sign_in.Authenticator;

import java.util.Arrays;

/** Public credentials for the local demo account. */
public final class DemoAccount implements Authenticator {
    public static final String USERNAME = "demo";
    public static final String PASSWORD = "accessgraph";

    @Override
    public boolean matches(String username, char[] password) {
        return USERNAME.equalsIgnoreCase(username) && Arrays.equals(PASSWORD.toCharArray(), password);
    }
}
