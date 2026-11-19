package view;

import interface_adapter.ViewManagerModel;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Switches which screen is showing whenever ViewManagerModel's state changes.
 */
public class ViewManager implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            String viewName = (String) evt.getNewValue();
            cardLayout.show(views, viewName);
        }
    }
}
