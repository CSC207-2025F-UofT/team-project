package app;

import data_access.FileUserDataAccessObject;
import interface_adapter.RandC_success_submit.RandCSuccessViewModel;
import interface_adapter.ViewManagerModel;

import interface_adapter.clicking.ClickingViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.rate_and_comment.CommentController;
import interface_adapter.rate_and_comment.CommentPresenter;
import interface_adapter.rate_and_comment.CommentViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.rate_and_comment.CommentInputBoundary;
import use_case.rate_and_comment.CommentInteractor;
import use_case.rate_and_comment.CommentOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;


public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject();
    //views and view models
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginView loginView;
    private LoginViewModel  loginViewModel;
    private WatchlistView watchlistView;
    private FavoritesView favoritesView;
    private RateAndCommentView rateAndCommentView;
    private CommentViewModel commentViewModel;
    private RandCSuccessSubmitView randCSuccessSubmitView;
    private RandCSuccessViewModel randCSuccessViewModel;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignUpView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
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

    public AppBuilder addRateAndCommentView() {
        commentViewModel = new CommentViewModel();
        rateAndCommentView = new RateAndCommentView(commentViewModel,clickingViewModel);
        cardPanel.add(rateAndCommentView, rateAndCommentView.getViewName());
        return this;
    }

    public AppBuilder addRandCView() {
        randCSuccessViewModel = new RandCSuccessViewModel();
        randCSuccessSubmitView = new RandCSuccessSubmitView(randCSuccessViewModel, clickingViewModel);
        cardPanel.add(randCSuccessSubmitView, randCSuccessViewModel.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase(){
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary signupInputBoundary = new SignupInteractor(userDataAccessObject,
                signupOutputBoundary);

        SignupController signupController = new SignupController(signupInputBoundary);
        signupView.setSignupController(signupController);
        return this;
    }

    public AppBuilder addLoginUseCase(){
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedinviewmodel, loginViewModel);
        final LoginInputBoundary loginInputBoundary = new LoginInteractor(userDataAccessObject,
                loginOutputBoundary);

        LoginController loginController = new LoginController(loginInputBoundary);
        loginView.setLoginController(loginController);
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
