package view;

import interface_adapter.ViewManagerModel;

import javax.swing.JPanel;
import java.awt.CardLayout;

/** Shows whichever view the {@link ViewManagerModel} names. Each view is added under its view model's name. */
public final class ViewManager extends JPanel {
    private final CardLayout cards = new CardLayout();

    public ViewManager(ViewManagerModel model) {
        setLayout(cards);
        model.addPropertyChangeListener(event ->
                SwingThreads.onEdt(() -> cards.show(this, (String) event.getNewValue())));
    }

    public void addView(String viewName, JPanel view) {
        view.setName(viewName);
        add(view, viewName);
    }
}
