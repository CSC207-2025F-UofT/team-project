package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import view.journal.JournalStyle;

/**
 * The home page: the app's name and one button per page (Map, Journal, ...).
 * Each use case's page is added by the app builder, so this class never changes
 * when a teammate adds a page.
 */
public class HomeView extends JPanel {
    private static final int BUTTON_GAP = 14;
    private static final int TITLE_GAP = 36;
    private static final int BUTTON_PAD_X = 70;
    private static final int BUTTON_PAD_Y = 14;

    private int nextRow = 1;

    /** Creates an empty home page with only the app's name. */
    public HomeView() {
        setLayout(new GridBagLayout());
        setBackground(JournalStyle.PAPER);
        setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));

        JLabel title = JournalStyle.label("Linked Out",
                JournalStyle.UI_FONT.deriveFont(Font.BOLD, 30f));
        GridBagConstraints titleLayout = new GridBagConstraints();
        titleLayout.gridy = 0;
        titleLayout.insets = new Insets(0, 0, TITLE_GAP, 0);
        add(title, titleLayout);
    }

    /**
     * Adds a button that opens a page.
     *
     * @param label the button text, usually the page's name
     * @param openPage shows the page
     */
    public void addPageButton(String label, Runnable openPage) {
        GridBagConstraints buttonLayout = new GridBagConstraints();
        buttonLayout.gridy = nextRow++;
        buttonLayout.fill = GridBagConstraints.HORIZONTAL;
        buttonLayout.ipadx = BUTTON_PAD_X;
        buttonLayout.ipady = BUTTON_PAD_Y;
        buttonLayout.insets = new Insets(0, 0, BUTTON_GAP, 0);
        add(JournalStyle.roundedButton(label, JournalStyle.PHOTO_BUTTON, openPage), buttonLayout);
    }
}
