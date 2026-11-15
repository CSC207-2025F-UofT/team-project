package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * The View Manager for the program. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 */
public class ViewManager implements ViewManagerModelListener{
    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;

    public ViewManager(CardLayout cardLayout, JPanel views, ViewManagerModel viewManagerModel) {
        this.cardLayout = cardLayout;
        this.views = views;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addListener(this);
    }

    @Override
    public void viewChanged(){
        cardLayout.show(views, viewManagerModel.getState());
    }
}
