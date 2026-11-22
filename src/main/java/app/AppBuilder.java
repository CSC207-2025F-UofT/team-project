package app;

import dataaccess.ApiStockDataAccessObject;
import dataaccess.FileRiskProfileSettingsDataAccessObject;
import interfaceadapter.setriskprofile.SetRiskProfileController;
import interfaceadapter.setriskprofile.SetRiskProfilePresenter;
import interfaceadapter.setriskprofile.SetRiskProfileViewModel;
import java.awt.CardLayout;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import usecase.setriskprofile.SetRiskProfileInputBoundary;
import usecase.setriskprofile.SetRiskProfileInteractor;
import usecase.shared.projection.TargetGainSellStrategy;
import view.SetRiskProfileView;

/**
 * Builds the application by wiring together the views, view models, presenters, interactors,
 * and data access objects.
 */
public class AppBuilder {
  private static final String RISK_PROFILE_SETTINGS_FILE = "risk_profile.json";

  private final CardLayout cardLayout = new CardLayout();
  private final JPanel cardPanel = new JPanel(cardLayout);

  private final ApiStockDataAccessObject stockDataAccessObject;
  private final FileRiskProfileSettingsDataAccessObject riskProfileSettingsDataAccessObject;

  private SetRiskProfileViewModel setRiskProfileViewModel;
  private SetRiskProfileView setRiskProfileView;
  private SetRiskProfileController setRiskProfileController;

  /**
   * Creates a new AppBuilder.
   *
   * @param apiKey Massive.com API key
   * @throws IOException If the configuration directory could not be created
   */
  public AppBuilder(String apiKey) throws IOException {
    stockDataAccessObject = new ApiStockDataAccessObject(apiKey);
    riskProfileSettingsDataAccessObject =
            new FileRiskProfileSettingsDataAccessObject(RISK_PROFILE_SETTINGS_FILE);
  }

  /**
   * Adds the Set Risk Profile view to the application.
   *
   * @return This builder
   */
  public AppBuilder addSetRiskProfileView() {
    setRiskProfileViewModel = new SetRiskProfileViewModel();
    setRiskProfileView = new SetRiskProfileView(setRiskProfileViewModel);
    cardPanel.add(setRiskProfileView, setRiskProfileView.getViewName());
    return this;
  }

  /**
   * Adds the Set Risk Profile use case to the application. Must be called after
   * {@link #addSetRiskProfileView()}.
   *
   * @return This builder
   */
  public AppBuilder addSetRiskProfileUseCase() {
    SetRiskProfilePresenter presenter = new SetRiskProfilePresenter(setRiskProfileViewModel);
    SetRiskProfileInputBoundary interactor = new SetRiskProfileInteractor(
            stockDataAccessObject, riskProfileSettingsDataAccessObject, presenter,
            new TargetGainSellStrategy());
    setRiskProfileController = new SetRiskProfileController(interactor);
    setRiskProfileView.setController(setRiskProfileController);
    return this;
  }

  /**
   * Builds the application window.
   *
   * @return The application's main frame
   */
  public JFrame build() {
    JFrame application = new JFrame("Stock Tracker");
    application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    application.add(cardPanel);

    if (setRiskProfileController != null) {
      // Restore the user's saved risk profile settings from the previous run.
      setRiskProfileController.loadSettings();
      cardLayout.show(cardPanel, setRiskProfileView.getViewName());
    }
    application.pack();
    application.setLocationRelativeTo(null);
    return application;
  }
}
