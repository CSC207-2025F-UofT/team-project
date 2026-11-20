package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import interface_adapter.ViewManagerModel;
import view.GalleryView;
import view.ViewManager;

/**
 * Builds the window and wires every screen. This is the only place concrete classes are put together.
 */
public class AppBuilder {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel views = new JPanel(cardLayout);
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(views, cardLayout, viewManagerModel);

    /**
     * Adds the pet gallery screen.
     *
     * @return this builder
     */
    public AppBuilder addGalleryView() {
        views.add(new GalleryView(), GalleryView.VIEW_NAME);
        return this;
    }

    /**
     * Creates the window showing the first screen.
     *
     * @return the main window, not yet visible
     */
    public JFrame build() {
        final JFrame frame = new JFrame("PawPortal");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(views);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        viewManagerModel.setActiveView(GalleryView.VIEW_NAME);
        return frame;
    }
}
