package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The pet gallery screen. For now it is a placeholder; the pet cards and filters come in later PRs.
 */
public class GalleryView extends JPanel {
    /** Name this screen is registered under in the view manager. */
    public static final String VIEW_NAME = "gallery";

    /**
     * Creates the placeholder gallery screen.
     */
    public GalleryView() {
        super(new BorderLayout());
        final JLabel title = new JLabel("Browse Pets", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.getAccessibleContext().setAccessibleName("Browse Pets heading");
        add(title, BorderLayout.CENTER);
        getAccessibleContext().setAccessibleName("Pet gallery");
    }
}
