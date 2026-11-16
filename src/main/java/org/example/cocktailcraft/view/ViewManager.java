package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.ViewManagerModel;
import org.example.cocktailcraft.interface_adapter.ViewManagerModelListener;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class ViewManager implements ViewManagerModelListener {
    private final JPanel views;
    private final CardLayout layout;
    private final ViewManagerModel model;

    public ViewManager(JPanel views, CardLayout layout, ViewManagerModel model) {
        this.views = views;
        this.layout = layout;
        this.model = model;
        model.addListener(this);
    }

    @Override
    public void viewChanged() {
        layout.show(views, model.getCurrentViewName());
    }
}
