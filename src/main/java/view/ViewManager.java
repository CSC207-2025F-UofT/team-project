package view;

import java.awt.CardLayout;

import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerModelListener;

/**
 * Shows the screen chosen in the {@link ViewManagerModel}. Screens are stacked in a CardLayout.
 */
public class ViewManager implements ViewManagerModelListener {
    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel model;

    /**
     * Creates a view manager and starts listening to the model.
     *
     * @param views the panel that holds every screen, using the given layout
     * @param cardLayout the layout of {@code views}
     * @param model the model that says which screen to show
     */
    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel model) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.model = model;
        model.addListener(this);
    }

    @Override
    public void stateChanged() {
        cardLayout.show(views, model.getActiveView());
    }
}
