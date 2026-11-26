package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.welcome.WelcomeViewModel;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
  private final JPanel cardPanel = new JPanel();
  private final CardLayout cardLayout = new CardLayout();

  /**
   * TODO: Initialize entities
   */

  final ViewManagerModel viewManagerModel = new ViewManagerModel();
  ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

  /**
   * TODO: Implement data access
   */

  /**
   * TODO: Initialize views
   */

  public AppBuilder() {
    cardPanel.setLayout(cardLayout);
  }

  public JFrame build() {
    final JFrame application = new JFrame("Welcome Screen");
    application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    application.add(cardPanel);

    viewManagerModel.setState(WelcomeViewModel.VIEW_NAME);
    viewManagerModel.notifyViewChanged();

    return application;
  }

}
