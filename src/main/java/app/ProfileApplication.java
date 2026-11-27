package app;

import interface_adapter.UiTasks;
import interface_adapter.profile.CreateProfileController;
import interface_adapter.profile.CreateProfilePresenter;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.profile.UpdateProfileController;
import interface_adapter.profile.UpdateProfilePresenter;
import interface_adapter.recommendation.RecommendationController;
import interface_adapter.recommendation.RecommendationPresenter;
import interface_adapter.recommendation.RecommendationViewModel;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import use_case.create_profile.CreateProfileInteractor;
import use_case.port.JobSource;
import use_case.port.PagedJobSearchSource;
import use_case.profile.ProfileRepository;
import use_case.recommendation.KeywordInternshipMatcher;
import use_case.recommendation.RecommendationInteractor;
import use_case.update_profile.UpdateProfileInteractor;
import view.LinkOpener;
import view.ProfileView;
import view.RecommendationView;

/** Wires profile creation, editing and recommendations alongside the existing Finder. */
public final class ProfileApplication {
  private ProfileApplication() {}

  /** Builds desktop tabs with a single board or offline Finder. */
  public static JTabbedPane createTabs(
      JobSource jobs,
      ProfileRepository profiles,
      Executor worker,
      Executor ui,
      LinkOpener links,
      String sourceLabel) {
    JComponent finder = FinderApplication.createView(jobs, worker, ui, links, sourceLabel);
    return withFinder(finder, jobs, profiles, worker, ui, links, sourceLabel);
  }

  /** Builds desktop tabs with both live search sources inside Finder. */
  public static JTabbedPane createLiveTabs(
      JobSource jobs,
      ProfileRepository profiles,
      Executor worker,
      Executor ui,
      LinkOpener links,
      String sourceLabel,
      Optional<PagedJobSearchSource> jooble) {
    JComponent finder =
        FinderApplication.createLiveView(jobs, jooble, worker, ui, links, sourceLabel);
    return withFinder(finder, jobs, profiles, worker, ui, links, sourceLabel);
  }

  private static JTabbedPane withFinder(
      JComponent finder,
      JobSource jobs,
      ProfileRepository profiles,
      Executor worker,
      Executor ui,
      LinkOpener links,
      String sourceLabel) {
    UiTasks tasks = new UiTasks(worker, ui);
    ProfileViewModel profile = new ProfileViewModel();
    CreateProfileController create =
        new CreateProfileController(
            new CreateProfileInteractor(profiles, new CreateProfilePresenter(profile, ui)),
            profile,
            tasks);
    UpdateProfileController update =
        new UpdateProfileController(
            new UpdateProfileInteractor(profiles, new UpdateProfilePresenter(profile, ui)),
            profile,
            tasks);

    RecommendationViewModel results = new RecommendationViewModel();
    RecommendationController recommend =
        new RecommendationController(
            new RecommendationInteractor(
                profiles,
                jobs,
                new KeywordInternshipMatcher(),
                new RecommendationPresenter(results, ui)),
            results,
            tasks);

    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Finder", finder);
    ProfileView profileView =
        new ProfileView(
            create,
            update,
            profile,
            () -> tabs.setSelectedIndex(tabs.indexOfTab("Recommendations")));
    RecommendationView recommendationView =
        new RecommendationView(
            recommend, results, links, sourceLabel, () -> tabs.setSelectedComponent(profileView));
    tabs.addTab("Profile", profileView);
    tabs.addTab("Recommendations", recommendationView);
    tabs.addChangeListener(
        event -> {
          if (tabs.getSelectedComponent() == recommendationView) {
            recommend.execute();
          }
        });
    update.loadProfile();
    return tabs;
  }
}
