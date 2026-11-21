package main.view;

import main.interface_adapters.ViewManagerModelListener;
import main.interface_adapters.ViewManagerModel;

import javax.swing.*;
import java.awt.*;

public class ViewManager implements ViewManagerModelListener {

    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addListener(this);
    }
    @Override

    public void viewChanged() {
        //this is the line of code that actually changes the view
        cardLayout.show(views, viewManagerModel.getActiveView());
    }
}
