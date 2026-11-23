package view;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Shows whichever screen ViewManagerModel names. All screens are stacked in one
 *  window like a deck of cards; only the one on top is visible.
 */
public class ViewManager implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;

    /**
     * Starts listening for screen changes.
     *
     * @param views the panel holding every screen
     * @param cardLayout that panel's layout
     * @param viewManagerModel says which screen to show
     */
    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            cardLayout.show(views, (String) evt.getNewValue());
        }
    }
}
