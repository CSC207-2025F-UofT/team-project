package view;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;

/**
 * Shows whichever view the ViewManagerModel says is active.
 */
public class ViewManager implements PropertyChangeListener {
    private final JPanel cards;
    private final CardLayout cardLayout;

    public ViewManager(JPanel cards, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.cards = cards;
        this.cardLayout = cardLayout;
        viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        cardLayout.show(cards, (String) evt.getNewValue());
    }
}
