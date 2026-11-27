package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * Shows whichever screen the ViewManagerModel says should be showing.
 * It's the only class that calls cardLayout.show(...).
 */
public class ViewManager implements ViewManagerModelListener {

    private final JPanel views;
    private final CardLayout cardLayout;
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates the ViewManager and starts listening to the model.
     *
     * @param views the panel that holds every screen
     * @param cardLayout the CardLayout used by that panel
     * @param viewManagerModel the model that says which screen to show
     */
    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addListener(this);
    }

    @Override
    public void viewChanged() {
        cardLayout.show(views, viewManagerModel.getCurrentViewName());
    }
}
