package view;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerModelListener;

/**
 * The View Manager for the program. It listens for changes in the ViewManagerModel
 * and updates which View should be visible.
 * Full screens are cards in one window; popups (like the result window) are dialogs on top of it.
 */
public class ViewManager implements ViewManagerModelListener {
    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;
    private final Map<String, JDialog> popups = new HashMap<>();

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addListener(this);
    }

    public void addPopup(String viewName, JDialog popup) {
        popups.put(viewName, popup);
    }

    @Override
    public void viewChanged() {
        String viewName = viewManagerModel.getCurrentViewName();
        JDialog popup = popups.get(viewName);
        if (popup != null) {
            popup.setLocationRelativeTo(views);
            popup.setVisible(true);
            return;
        }
        for (JDialog open : popups.values()) {
            open.setVisible(false);
        }
        cardLayout.show(views, viewName);
    }
}
