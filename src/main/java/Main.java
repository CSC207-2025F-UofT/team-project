import javax.swing.*;
import java.awt.*;

public class Main {
    public static void createAndShowGUI() { //user story 6
        //Create and set up the window.
        JFrame frame = new JFrame("Tier List");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create the ItemList which now includes both model and GUI
        ItemList itemList = new ItemList();

        frame.add(itemList);

        frame.pack();

        frame.setSize(350, 750);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}
