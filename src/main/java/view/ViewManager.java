package view;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;

/**
 * Shows the screen chosen in the {@link ViewManagerModel}. Screens are stacked in a CardLayout.
 */
public class ViewManager implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;

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
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (ViewManagerModel.ACTIVE_VIEW.equals(event.getPropertyName())) {
            cardLayout.show(views, (String) event.getNewValue());
        }
    }
}
