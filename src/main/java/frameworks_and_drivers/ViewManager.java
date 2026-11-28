package frameworks_and_drivers;

import javax.swing.*;
import java.awt.*;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModelManagerListener;

public class ViewManager implements ViewModelManagerListener {
    private final CardLayout cardlayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;

    public ViewManager(JPanel views, CardLayout cardlayout, ViewManagerModel viewManagerModel) {
        this.cardlayout = cardlayout;
        this.views = views;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addListener(this);
    }
    @Override
    public void viewChanged() {
        cardlayout.show(views, viewManagerModel.getCurrentViewName());
    }
}
