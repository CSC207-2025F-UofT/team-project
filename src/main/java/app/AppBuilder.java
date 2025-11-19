package app;

import data_access.ClickingDataAccessTMDb;
import data_access.FileUserDataAccessObject;
import entity.MediaDetailsResponse;
import interface_adapter.RandC_success_submit.RandCSuccessViewModel;
import interface_adapter.ViewManagerModel;

import interface_adapter.browse.BrowseViewModel;
import interface_adapter.clicking.ClickingState;
import interface_adapter.rate_and_comment.CommentController;
import interface_adapter.rate_and_comment.CommentPresenter;
import interface_adapter.rate_and_comment.CommentViewModel;
import interface_adapter.clicking.ClickingPresenter;
import interface_adapter.clicking.ClickingController;
import interface_adapter.clicking.ClickingViewModel;
import use_case.clicking.*;
import use_case.rate_and_comment.CommentInputBoundary;
import use_case.rate_and_comment.CommentInteractor;
import use_case.rate_and_comment.CommentOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject();

    private WatchlistView watchlistView;
    private FavoritesView favoritesView;
    private BrowseView browseView;
    private RateAndCommentView rateAndCommentView;
    private HomepageView homepageView;
    private CommentViewModel commentViewModel;
    private RandCSuccessSubmitView randCSuccessSubmitView;
    private RandCSuccessViewModel randCSuccessViewModel;
    private ClickingView clickingView;
    private ClickingViewModel clickingViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addClickingView() {
        clickingViewModel = new ClickingViewModel();
        clickingView = new ClickingView(clickingViewModel);
        cardPanel.add(clickingView, clickingView.getViewName());

        ClickingDataAccessInterface dataAccess = new ClickingDataAccessTMDb();
        new Thread(() -> {
            MediaDetailsResponse movie = dataAccess.fetchDetailsById(550);
            if (movie != null) {
                SwingUtilities.invokeLater(() -> {
                    ClickingState initState = clickingViewModel.getState();
                    initState.setTitle(movie.getTitle());
                    initState.setOverview(movie.getOverview());
                    initState.setYear(movie.getReleaseYear());
                    initState.setRating(Double.parseDouble(String.valueOf(movie.getRating())));
                    initState.setLanguage(movie.getLanguage());
                    initState.setGenres(movie.getGenres());
                    initState.setPosterUrl(movie.getPosterUrl());
                    clickingViewModel.firePropertyChange();
                });
            }
        }).start();

        return this;
    }

    public AppBuilder addClickingUseCase() {
        final ClickingDataAccessInterface clickingDataAccess = new ClickingDataAccessTMDb();

        final ClickingOutputBoundary clickingPresenter = new ClickingPresenter(
                clickingViewModel, viewManagerModel);
        final ClickingInputBoundary clickingInteractor = new ClickingInteractor(
                clickingPresenter, clickingDataAccess);

        final ClickingController clickingController = new ClickingController(clickingInteractor);
        clickingView.setClickingController(clickingController);
        return this;
    }

    public AppBuilder addWatchlistView() {
        watchlistView = new WatchlistView();
        cardPanel.add(watchlistView, watchlistView.getViewName());
        return this;
    }

    public AppBuilder addFavoritesView() {
        favoritesView = new FavoritesView();
        cardPanel.add(favoritesView, favoritesView.getViewName());
        return this;
    }

//    public AppBuilder addBrowseView() {
//        browseView = new BrowseView();
//        cardPanel.add(browseView, browseView.getViewname());
//        return this;
//    }

    public AppBuilder addRateAndCommentView(String un, String mn) {
        commentViewModel = new CommentViewModel();
        rateAndCommentView = new RateAndCommentView(commentViewModel, un, mn);
        cardPanel.add(rateAndCommentView, rateAndCommentView.getViewName());
        return this;
    }

    public AppBuilder addHomepageView() {
        homepageView = new HomepageView();
        cardPanel.add(homepageView, homepageView.getViewName());

        // Use the actual getViewName() for the two existing views.
        // For browse: you said it's "in progress" so either add a browse view
        // or use homepageView.getViewName() as a placeholder.
        homepageView.setBrowseButtonListener(e -> {
            viewManagerModel.setState("BROWSE");
        });

        // These two will use the actual view names (safer than hard-coded strings)
        homepageView.setWatchlistButtonListener(e -> {
            viewManagerModel.setState(watchlistView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        homepageView.setFavoritesButtonListener(e -> {
            viewManagerModel.setState(favoritesView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        return this;
    }


    public AppBuilder addRandCView(String movie) {
        randCSuccessViewModel = new RandCSuccessViewModel();
        randCSuccessSubmitView = new RandCSuccessSubmitView(movie);
        cardPanel.add(randCSuccessSubmitView, randCSuccessViewModel.getViewName());
        return this;
    }

    public AppBuilder addCommentUseCase() {
        final CommentOutputBoundary commentOutputBoundary = new CommentPresenter(commentViewModel,
                randCSuccessViewModel, viewManagerModel);
        final CommentInputBoundary commentInputBoundary = new CommentInteractor(userDataAccessObject,
                commentOutputBoundary);

        CommentController commentController = new CommentController(commentInputBoundary);
        rateAndCommentView.setCommentController(commentController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Homepage");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        application.setSize(800, 600);
        application.setLocationRelativeTo(null);


        SwingUtilities.invokeLater(() -> {
            if (clickingView != null) {
                viewManagerModel.setState(clickingView.getViewName());
                viewManagerModel.firePropertyChange();
            }
        });

        application.setVisible(true);
        return application;
    }
}
