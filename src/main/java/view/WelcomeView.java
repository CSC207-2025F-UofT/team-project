package view;

import interface_adapter.welcome.WelcomeController;
import interface_adapter.welcome.WelcomeViewModel;
import interface_adapter.welcome.WelcomeViewModelListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for the Welcome Screen
 */
public class WelcomeView extends JPanel implements ActionListener, WelcomeViewModelListener {

  private final WelcomeViewModel welcomeViewModel;
  private WelcomeController welcomeController = null;

  public WelcomeView(WelcomeViewModel welcomeViewModel) {
    this.welcomeViewModel = welcomeViewModel;
    welcomeViewModel.addListener(this);

    final JLabel title = new JLabel(WelcomeViewModel.TITLE_LABEL);
    title.setAlignmentX(JComponent.CENTER_ALIGNMENT);

    /**
     * TODO: add fields, buttons, and listeners
     */

  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    /**
     * TODO: implement the override
     */
  }

  @Override
  public void stateChanged() {
    /**
     * TODO: implement the override
     */
  }

  public void setWelcomeController(WelcomeController controller) {
    this.welcomeController = controller;
  }
}
