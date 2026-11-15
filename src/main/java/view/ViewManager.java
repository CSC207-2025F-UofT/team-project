package view;

import interface_adapter.ViewManagerModel;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View Manager for the program. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 */
public class ViewManager implements PropertyChangeListener {
  private final JPanel cardPanel;
  private final CardLayout cardLayout;

  /**
   * Creates the view manager and starts listening to the model.
   *
   * @param cardPanel        the panel holding all views, each added under its view name
   * @param cardLayout       the layout of {@code cardPanel}
   * @param viewManagerModel the model that says which view should be shown
   */
  public ViewManager(JPanel cardPanel, CardLayout cardLayout,
                     ViewManagerModel viewManagerModel) {
    this.cardPanel = cardPanel;
    this.cardLayout = cardLayout;
    viewManagerModel.addPropertyChangeListener(this);
  }

  /**
   * Shows the view that the model now names.
   *
   * @param evt the change event from the view manager model
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (ViewManagerModel.VIEW_PROPERTY.equals(evt.getPropertyName())) {
      cardLayout.show(cardPanel, (String) evt.getNewValue());
    }
  }
}