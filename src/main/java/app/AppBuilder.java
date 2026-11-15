package app;

import data_access.InMemoryStudySetDataAccessObject;
import entity.StudySetFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_study_set.CreateStudySetController;
import interface_adapter.create_study_set.CreateStudySetPresenter;
import interface_adapter.create_study_set.CreateStudySetState;
import interface_adapter.create_study_set.CreateStudySetViewModel;
import interface_adapter.study_set.StudySetViewModel;
import use_case.create_study_set.CreateStudySetInteractor;
import view.CreateStudySetView;
import view.StudySetView;
import view.ViewManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.CardLayout;

import javax.swing.JTabbedPane;
import java.awt.Dimension;

/**
 * Builds the application by creating each layer's objects and connecting them.
 */
public class AppBuilder {
  private final CardLayout cardLayout = new CardLayout();
  private final JPanel cardPanel = new JPanel(cardLayout);
  private final ViewManagerModel viewManagerModel = new ViewManagerModel();
  private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
  private final InMemoryStudySetDataAccessObject studySetDao =
      new InMemoryStudySetDataAccessObject();

  private CreateStudySetViewModel createStudySetViewModel;
  private StudySetViewModel studySetViewModel;
  private CreateStudySetView createStudySetView;
  private StudySetView studySetView;

  /**
   * Creates an empty builder.
   */
  public AppBuilder() { }

  /**
   * Returns the id of the logged-in user.
   *
   * @return the current user's id
   */
  private String currentUserId() {
    return "demo-user";   // TODO: replace with the logged-in user's id from Supabase Auth
  }

  /**
   * Opens a study set that the user clicked in the panel: marks it as the open
   * study set and switches to the study set view.
   *
   * @param item the study set that was clicked
   */
  private void openStudySet(CreateStudySetState.Item item) {
    studySetViewModel.getState().open(item.getId(), item.getName(), item.getCardCount());
    studySetViewModel.firePropertyChanged();
    viewManagerModel.setActiveView(StudySetViewModel.VIEW_NAME);
  }

  /**
   * Creates the create study set view and its view model. Clicking a study set
   * in its panel opens it.
   *
   * @return this builder, for chaining
   */
  public AppBuilder addCreateStudySetView() {
    createStudySetViewModel = new CreateStudySetViewModel();
    createStudySetView = new CreateStudySetView(
        createStudySetViewModel,
        this::currentUserId,
        this::openStudySet);
    cardPanel.add(createStudySetView, CreateStudySetView.VIEW_NAME);
    return this;
  }

  /**
   * Creates the study set view and its view model. Its Back button returns to the
   * create study set view.
   *
   * @return this builder, for chaining
   */
  public AppBuilder addStudySetView() {
    studySetViewModel = new StudySetViewModel();
    studySetView = new StudySetView(
        studySetViewModel,
        () -> viewManagerModel.setActiveView(CreateStudySetView.VIEW_NAME));
    cardPanel.add(studySetView, StudySetView.VIEW_NAME);
    return this;
  }

  /**
   * Creates the Create Study Set interactor, presenter and controller, and gives the
   * controller to the view. Requires {@link #addCreateStudySetView()} and
   * {@link #addStudySetView()} to have been called.
   *
   * @return this builder, for chaining
   */
  public AppBuilder addCreateStudySetUseCase() {
    final CreateStudySetPresenter presenter = new CreateStudySetPresenter(
        createStudySetViewModel, studySetViewModel, viewManagerModel);
    final CreateStudySetInteractor interactor =
        new CreateStudySetInteractor(studySetDao, presenter, new StudySetFactory());
    final CreateStudySetController controller = new CreateStudySetController(interactor);
    createStudySetView.setController(controller);
    return this;
  }

  /**
   * Builds the application window, starting on the create study set view.
   *
   * @return the window, not yet visible
   */
  public JFrame build() {
    final JTabbedPane pages = new JTabbedPane();
    pages.addTab("Study sets", cardPanel);
    pages.addTab("Upload PDF", new JPanel());   // placeholder for a future use case
    pages.addTab("Review", new JPanel());       // placeholder for a future use case

    final JFrame frame = new JFrame("Study App");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(pages);
    frame.setMinimumSize(new Dimension(900, 620));
    frame.setSize(1100, 720);
    viewManagerModel.setActiveView(CreateStudySetView.VIEW_NAME);
    return frame;
  }
}