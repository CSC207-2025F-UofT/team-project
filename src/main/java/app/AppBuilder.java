package app;

import javax.swing.*;

/**
 * Builds the whole app: creates each screen and connects its
 * Controller -> Interactor -> Presenter -> ViewModel -> View.
 */
public class AppBuilder {
    public AppBuilder() {}
    public AppBuilder addAddClothingView() {return this;}
    public AppBuilder addGenerateOutfitView() {return this;}
    public AppBuilder addSetStyleView() {return this;}
    public AppBuilder addSaveOutfitView() {return this;}
    public AppBuilder addViewClosetView() {return this;}
//    public JFrame build() {}
}

