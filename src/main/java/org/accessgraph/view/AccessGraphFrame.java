package org.accessgraph.view;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Desktop window; Swing components are constructed and updated on the event dispatch thread. */
public final class AccessGraphFrame extends JFrame {
    public AccessGraphFrame(AccessGraphPanel content) {
        super("AccessGraph");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(content);
        addWindowListener(new WindowAdapter() {
            @Override public void windowOpened(WindowEvent event) { content.focusSignIn(); }
            @Override public void windowClosing(WindowEvent event) { content.close(); }
            @Override public void windowClosed(WindowEvent event) { content.close(); }
        });
        setMinimumSize(new Dimension(740, 520));
        setSize(1050, 700);
        setLocationRelativeTo(null);
    }
}
