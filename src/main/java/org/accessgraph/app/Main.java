package org.accessgraph.app;

import org.accessgraph.data_access.DemoAccount;
import org.accessgraph.view.AccessGraphFrame;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        if (args.length == 1 && ("--help".equals(args[0]) || "-h".equals(args[0]))) {
            System.out.println("AccessGraph map demo\nRun java -jar target/accessgraph.jar with Java 11 or newer.\n"
                    + "Demo username " + DemoAccount.USERNAME + ", password " + DemoAccount.PASSWORD + ".\n"
                    + "Allow location in your browser to center the map on your device.");
            return;
        }
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("AccessGraph requires a graphical desktop to open its Swing window. "
                    + "Run mvn test for headless verification.");
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> new AccessGraphFrame(AppFactory.createPanel()).setVisible(true));
    }
}
