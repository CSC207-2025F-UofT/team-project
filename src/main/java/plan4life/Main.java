package plan4life;

import javax.swing.SwingUtilities;
import plan4life.view.CalendarFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalendarFrame());
    }
}