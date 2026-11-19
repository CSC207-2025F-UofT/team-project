package view;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;

/**
 * Switches screens whenever the ViewManagerModel state changes.
 * Full screens are cards in one window; popups (like the result window) are dialogs on top of it.
 */
public class ViewManager implements PropertyChangeListener {
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final Map<String, JDialog> popups = new HashMap<>();

    public ViewManager(JPanel cardPanel, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        viewManagerModel.addPropertyChangeListener(this);
    }

    public void addPopup(String viewName, JDialog popup) {
        popups.put(viewName, popup);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }
        String viewName = (String) evt.getNewValue();
        JDialog popup = popups.get(viewName);
        if (popup != null) {
            popup.setLocationRelativeTo(cardPanel);
            popup.setVisible(true);
            return;
        }
        for (JDialog open : popups.values()) {
            open.setVisible(false);
        }
        cardLayout.show(cardPanel, viewName);
    }
}
