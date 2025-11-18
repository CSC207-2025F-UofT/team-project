package app;

import data_access.FileUserDataAccessObject;
import interface_adapter.RandC_success_submit.RandCSuccessViewModel;
import interface_adapter.ViewManagerModel;

import interface_adapter.rate_and_comment.CommentController;
import interface_adapter.rate_and_comment.CommentPresenter;
import interface_adapter.rate_and_comment.CommentViewModel;
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
    private RateAndCommentView rateAndCommentView;
    private CommentViewModel commentViewModel;
    private RandCSuccessSubmitView randCSuccessSubmitView;
    private RandCSuccessViewModel randCSuccessViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
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

    public AppBuilder addRateAndCommentView(String un, String mn) {
        commentViewModel = new CommentViewModel();
        //TODO 将emptystring替换成实际的名字
        rateAndCommentView = new RateAndCommentView(commentViewModel, un, mn);
        cardPanel.add(rateAndCommentView, rateAndCommentView.getViewName());
        return this;
    }

    public AppBuilder addRandCView(String movie) {
        randCSuccessViewModel = new RandCSuccessViewModel();
        randCSuccessSubmitView = new RandCSuccessSubmitView(movie);
        cardPanel.add(randCSuccessSubmitView, randCSuccessViewModel.getViewName());
        return this;
    }

    public AppBuilder addCommentUseCase(){
        final CommentOutputBoundary commentOutputBoundary = new CommentPresenter(commentViewModel,
                randCSuccessViewModel, viewManagerModel);
        final CommentInputBoundary commentInputBoundary = new CommentInteractor(userDataAccessObject,
                commentOutputBoundary);

        CommentController commentController = new CommentController(commentInputBoundary);
        rateAndCommentView.setCommentController(commentController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Watchlist");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 1. ADD THE CARD CONTAINER TO THE FRAME FIRST
        application.add(cardPanel);

        // 2. Set preferred size and center the window
        application.pack();
        application.setSize(800, 600);
        application.setLocationRelativeTo(null);

        // 3. SCHEDULE the view switch for later (Guaranteed fix)
        SwingUtilities.invokeLater(() -> {
            // This code runs *after* the JFrame is displayed and ready.
            if (watchlistView != null) {
                viewManagerModel.setState(watchlistView.getViewName());
                viewManagerModel.firePropertyChange();
            }
        });

        // 4. Make the window visible (must be outside the invokeLater)
        application.setVisible(true);

        return application;
    }
}
