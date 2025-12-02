package view.common;

import service.Backend;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class UIComponent extends JPanel {
    protected final Backend backend;
    protected final JFrame frame;

    public UIComponent(JFrame frame, Backend backend) {
        this.backend = backend;
        this.frame = frame;
    }
}
