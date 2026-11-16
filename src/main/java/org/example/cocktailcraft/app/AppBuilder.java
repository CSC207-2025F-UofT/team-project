package org.example.cocktailcraft.app;

import org.example.cocktailcraft.interface_adapter.NavigationController;
import org.example.cocktailcraft.interface_adapter.ViewManagerModel;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchPresenter;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.interface_adapter.browse.BrowseController;
import org.example.cocktailcraft.interface_adapter.browse.BrowsePresenter;
import org.example.cocktailcraft.interface_adapter.browse.BrowseViewModel;
import org.example.cocktailcraft.interface_adapter.details.DetailsController;
import org.example.cocktailcraft.interface_adapter.details.DetailsPresenter;
import org.example.cocktailcraft.interface_adapter.details.DetailsViewModel;
import org.example.cocktailcraft.interface_adapter.favourite.FavouriteController;
import org.example.cocktailcraft.interface_adapter.favourite.FavouritePresenter;
import org.example.cocktailcraft.interface_adapter.favourite.FavouriteViewModel;
import org.example.cocktailcraft.interface_adapter.signup.SignupController;
import org.example.cocktailcraft.interface_adapter.signup.SignupPresenter;
import org.example.cocktailcraft.interface_adapter.signup.SignupViewModel;
import org.example.cocktailcraft.interface_adapter.login.LoginController;
import org.example.cocktailcraft.interface_adapter.login.LoginPresenter;
import org.example.cocktailcraft.interface_adapter.login.LoginViewModel;
import org.example.cocktailcraft.interface_adapter.logout.LogoutController;
import org.example.cocktailcraft.interface_adapter.logout.LogoutPresenter;
import org.example.cocktailcraft.interface_adapter.logout.LogoutViewModel;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeController;
import org.example.cocktailcraft.interface_adapter.customize.CustomizePresenter;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeViewModel;
import org.example.cocktailcraft.use_case.search.SearchInteractor;
import org.example.cocktailcraft.use_case.browse.BrowseInteractor;
import org.example.cocktailcraft.use_case.details.DetailsInteractor;
import org.example.cocktailcraft.use_case.favourite.FavouriteInteractor;
import org.example.cocktailcraft.use_case.signup.SignupInteractor;
import org.example.cocktailcraft.use_case.login.LoginInteractor;
import org.example.cocktailcraft.use_case.logout.LogoutInteractor;
import org.example.cocktailcraft.use_case.customize.CustomizeInteractor;
import org.example.cocktailcraft.view.MainPageView;
import org.example.cocktailcraft.view.SearchView;
import org.example.cocktailcraft.view.ProfileView;
import org.example.cocktailcraft.view.LoginView;
import org.example.cocktailcraft.view.SignupView;
import org.example.cocktailcraft.view.CustomizeView;
import org.example.cocktailcraft.view.CocktailDetailsView;
import org.example.cocktailcraft.view.ViewManager;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Composition root: only construction and wiring, no use case rules. */
public class AppBuilder {
    public JFrame build() {
        ViewManagerModel navigationModel = new ViewManagerModel();
        NavigationController navigation = new NavigationController(navigationModel);
        SearchViewModel searchModel = new SearchViewModel();
        SearchController search = new SearchController(
                new SearchInteractor(new SearchPresenter(searchModel)));

        BrowseViewModel browseModel = new BrowseViewModel();
        BrowseController browse = new BrowseController(
                new BrowseInteractor(new BrowsePresenter(browseModel)));

        DetailsViewModel detailsModel = new DetailsViewModel();
        DetailsController details = new DetailsController(
                new DetailsInteractor(new DetailsPresenter(detailsModel)));

        FavouriteViewModel favouriteModel = new FavouriteViewModel();
        FavouriteController favourite = new FavouriteController(
                new FavouriteInteractor(new FavouritePresenter(favouriteModel)));

        SignupViewModel signupModel = new SignupViewModel();
        SignupController signup = new SignupController(
                new SignupInteractor(new SignupPresenter(signupModel)));

        LoginViewModel loginModel = new LoginViewModel();
        LoginController login = new LoginController(
                new LoginInteractor(new LoginPresenter(loginModel)));

        LogoutViewModel logoutModel = new LogoutViewModel();
        LogoutController logout = new LogoutController(
                new LogoutInteractor(new LogoutPresenter(logoutModel)));

        CustomizeViewModel customizeModel = new CustomizeViewModel();
        CustomizeController customize = new CustomizeController(
                new CustomizeInteractor(new CustomizePresenter(customizeModel)));

        CardLayout layout = new CardLayout();
        JPanel views = new JPanel(layout);
        views.add(new MainPageView(new SearchView(search, searchModel), browse, browseModel, navigation),
                MainPageView.VIEW_NAME);
        views.add(new CocktailDetailsView(details, detailsModel, favourite, favouriteModel, navigation),
                CocktailDetailsView.VIEW_NAME);
        views.add(new ProfileView(logout, logoutModel, navigation), ProfileView.VIEW_NAME);
        views.add(new LoginView(login, loginModel, navigation), LoginView.VIEW_NAME);
        views.add(new SignupView(signup, signupModel, navigation), SignupView.VIEW_NAME);
        views.add(new CustomizeView(customize, customizeModel, navigation), CustomizeView.VIEW_NAME);
        new ViewManager(views, layout, navigationModel);
        navigation.show(MainPageView.VIEW_NAME);

        JFrame frame = new JFrame("CocktailCraft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(views);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}
