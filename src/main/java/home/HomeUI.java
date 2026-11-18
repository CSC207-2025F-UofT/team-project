package home;

import javax.swing.*;
import java.awt.*;

public class HomeUI extends JPanel {
    public HomeUI() {
//        this.setLayout(new GridBagLayout());
        this.add(new JLabel("The Home Page"));

        JButton editDeckButton = new JButton("Edit deck");
        JButton battleButton = new JButton("Battle");

        this.add(editDeckButton);
        this.add(battleButton);
    }
}
