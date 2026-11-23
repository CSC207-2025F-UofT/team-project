package app;

import dataaccess.ApiStockDataAccessObject;
import dataaccess.CachedStockHistoryDataAccessObject;
import dataaccess.EstimateRoiDataAccessObject;
import dataaccess.FileRiskProfileSettingsDataAccessObject;
import dataaccess.FileWatchlistDataAccessObject;
import interfaceadapter.estimateroi.EstimateRoiController;
import interfaceadapter.estimateroi.EstimateRoiPresenter;
import interfaceadapter.estimateroi.EstimateRoiViewModel;
import interfaceadapter.setriskprofile.SetRiskProfileController;
import interfaceadapter.setriskprofile.SetRiskProfilePresenter;
import interfaceadapter.setriskprofile.SetRiskProfileViewModel;
import interfaceadapter.viewstockinfo.ViewStockInfoController;
import interfaceadapter.viewstockinfo.ViewStockInfoPresenter;
import interfaceadapter.viewstockinfo.ViewStockInfoViewModel;
import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import usecase.estimateroi.EstimateRoiInputBoundary;
import usecase.estimateroi.EstimateRoiInteractor;
import usecase.setriskprofile.SetRiskProfileInputBoundary;
import usecase.setriskprofile.SetRiskProfileInteractor;
import usecase.shared.projection.TargetGainSellStrategy;
import usecase.viewstockinfo.ViewStockInfoInputBoundary;
import usecase.viewstockinfo.ViewStockInfoInteractor;
import usecase.viewstockinfo.ViewStockInfoOutputData;
import view.EstimateRoiView;
import view.SetRiskProfileView;
import view.ViewStockInfoView;

/**
 * Builds the application by wiring together the views, view models, presenters, interactors,
 * and data access objects.
 */
public class AppBuilder {
  private static final String RISK_PROFILE_SETTINGS_FILE = "risk_profile.json";
  private static final String WATCHLIST_FILE = "watchlist.json";

  private final JTabbedPane tabs = new JTabbedPane();

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
    tabs.addTab("Risk Profile", setRiskProfileView);
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
   * Adds and wires the Stock Info tab, including the Estimate ROI panel.
   *
   * @return This builder
   * @throws IOException If the watchlist directory cannot be opened
   */
  public AppBuilder addViewStockInfoUseCase() throws IOException {
    ViewStockInfoViewModel viewModel = new ViewStockInfoViewModel();
    ViewStockInfoPresenter presenter = new ViewStockInfoPresenter(viewModel);
    ViewStockInfoInputBoundary interactor = new ViewStockInfoInteractor(
            new CachedStockHistoryDataAccessObject(stockDataAccessObject),
            new FileWatchlistDataAccessObject(WATCHLIST_FILE), presenter);
    ViewStockInfoController controller = new ViewStockInfoController(interactor);
    ViewStockInfoView view = new ViewStockInfoView(viewModel, controller);

    // Estimate ROI (Use Case 4)
    EstimateRoiViewModel roiViewModel = new EstimateRoiViewModel();
    EstimateRoiInputBoundary roiInteractor = new EstimateRoiInteractor(
            new EstimateRoiDataAccessObject(stockDataAccessObject),
            new EstimateRoiPresenter(roiViewModel));
    EstimateRoiView roiView = new EstimateRoiView(roiViewModel,
            new EstimateRoiController(roiInteractor));

    // Tell the ROI panel which stock the user is currently viewing
    viewModel.addPropertyChangeListener(evt -> {
      if (ViewStockInfoViewModel.RESULT_PROPERTY.equals(evt.getPropertyName())) {
        ViewStockInfoOutputData result = viewModel.getState().getResult();
        String ticker = result == null ? null : result.getTicker();
        SwingUtilities.invokeLater(() -> roiView.setTicker(ticker));
      }
    });

    // Put the Stock Info view and the ROI panel together in one tab
    JPanel stockTab = new JPanel(new BorderLayout());
    stockTab.add(view, BorderLayout.CENTER);
    stockTab.add(roiView, BorderLayout.SOUTH);

    tabs.addChangeListener(event -> {
      if (tabs.getSelectedComponent() == stockTab) {
        controller.loadWatchlist();
      }
    });
    tabs.addTab("Stock Info", stockTab);
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
    application.add(tabs);

    if (setRiskProfileController != null) {
      // Restore the user's saved risk profile settings from the previous run.
      setRiskProfileController.loadSettings();
      tabs.setSelectedComponent(setRiskProfileView);
    }
    application.pack();
    application.setLocationRelativeTo(null);
    return application;
  }
}
