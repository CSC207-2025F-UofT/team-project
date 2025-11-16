package view;

import interface_adapter.blank.BlankViewModel;

import javax.swing.*;
import java.awt.*;

public class BlankView extends JPanel {
    private final BlankViewModel viewModel;

    public BlankView(BlankViewModel viewModel) {
        this.viewModel = viewModel;
        setBackground(Color.WHITE); // plain white screen
        // no components added → totally blank
    }

    public String getViewName() {
        return viewModel.getViewName();
    }
}
