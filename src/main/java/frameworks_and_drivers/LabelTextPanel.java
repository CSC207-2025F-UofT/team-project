package frameworks_and_drivers;

import javax.swing.*;
import java.awt.*;

/**
 * A panel containing a label and a text field.
 */
class LabelTextPanel extends JPanel {
    LabelTextPanel(JLabel label, JTextField textField) {

        label.setPreferredSize(new Dimension(120, 25));
        textField.setPreferredSize(new Dimension(200, 25));

        this.add(label);
        this.add(textField);
    }
}
