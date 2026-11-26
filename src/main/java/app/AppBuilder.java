package app;

import data_access.ClosetData;
import data_access.UserImages;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import interface_adapter.add_clothing.AddClothingController;
import interface_adapter.add_clothing.AddClothingPresenter;
import interface_adapter.add_clothing.AddClothingViewModel;
import use_case.add_clothing.AddClothingInputBoundary;
import use_case.add_clothing.AddClothingInteractor;
import use_case.add_clothing.AddClothingOutputBoundary;
import view.AddClothingView;
import view.ViewManager;
import view.WelcomeView;

import javax.swing.*;
import java.awt.*;

/**
 * Builds the whole app: creates each screen and connects its
 * Controller -> Interactor -> Presenter -> ViewModel -> View.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
//    setting the layout?
    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        new ViewManager(cardPanel, cardLayout, viewManagerModel);
    }

    // welcome screen
//    private WelcomeViewModel welcomeViewModel;
//    private WelcomeView welcomeView;
//    public AppBuilder addWelcomeView() {
//        welcomeViewModel = new WelcomeViewModel();
//        welcomeView = new WelcomeView(welcomeViewModel);
//        welcomeView.setWelcomeController(new WelcomeController(viewManagerModel));
//        cardPanel.add(welcomeView, welcomeView.getViewName());
//        return this;
//    }
    // Ginni: add clothing
    private AddClothingViewModel addClothingViewModel;
    private AddClothingView addClothingView;
    public AppBuilder addAddClothingView() {
        addClothingViewModel = new AddClothingViewModel();
        addClothingView = new AddClothingView(addClothingViewModel);
        cardPanel.add(addClothingView, addClothingView.getViewName());
        return this;
    }
    /** Connects Add Clothing's controller, interactor and presenter (call after addAddClothingView)*/
    private final ClosetData closetData = new ClosetData();
    public AppBuilder addAddClothingUseCase() {
        final AddClothingOutputBoundary presenter =
                new AddClothingPresenter(addClothingViewModel, viewManagerModel);
        final AddClothingInputBoundary interactor =
                new AddClothingInteractor(closetData, new UserImages(), presenter);
        addClothingView.setAddClothingController(new AddClothingController(interactor));
        return this;
    }
    public AppBuilder addGenerateOutfitView() {return this;}
    public AppBuilder addSetStyleView() {return this;}
    public AppBuilder addSaveOutfitView() {return this;}
    public AppBuilder addViewClosetView() {return this;}


    // for evryone so dont change it!
    public JFrame build() {
        final JFrame frame = new JFrame("Sweater Weather");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(cardPanel);

        viewManagerModel.setState(ViewNames.WELCOME);
        viewManagerModel.firePropertyChanged();
        return frame;
    }
}

