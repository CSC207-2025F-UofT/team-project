package main.view;

import main.interface_adapters.MainMenuViewModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuView extends JPanel {

    private final String viewName = "Main Menu";
    private final MainMenuViewModel viewModel;
    public final JButton play;

    public MainMenuView(MainMenuViewModel mainMenuViewModel) {
        this.viewModel = mainMenuViewModel;

        final JLabel title = new JLabel(viewModel.TITLE_LABEL);
        title.setAlignmentX(CENTER_ALIGNMENT);

        final JPanel button = new JPanel();
        this.play = new JButton(viewModel.PLAY_LABEL);
        button.add(this.play);

        this.play.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(play)) {
                            System.out.println("User pressed Play");
                        }
                    }
                }
        );

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(button);

    }
}
