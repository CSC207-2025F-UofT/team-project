package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeState;
import interface_adapter.home.HomeViewModel;
import interface_adapter.logout.LogOutController;
import interface_adapter.settings.SettingsViewModel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public final class HomeView extends JPanel {
    private final JLabel signedInAs = new JLabel();
    private final JLabel handle = new JLabel();
    private final JButton settings = new JButton("Settings");
    private final JButton logOut = new JButton("Log Out");

    public HomeView(HomeViewModel viewModel, LogOutController controller, ViewManagerModel viewManagerModel) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(16, 20, 16, 20));
        add(new JLabel("Home"));
        signedInAs.setName("Signed in as");
        add(signedInAs);
        handle.setName("Handle");
        add(handle);
        add(settings);
        add(logOut);

        // Change Password (#22): way to get to Settings until we have the sidebar
        settings.addActionListener(event -> viewManagerModel.setState(SettingsViewModel.VIEW_NAME));

        logOut.addActionListener(event -> {
            logOut.setEnabled(false);
            SwingThreads.inBackground(controller::logOut, () -> { }, () -> logOut.setEnabled(true));
        });
        viewModel.addPropertyChangeListener(event ->
                SwingThreads.onEdt(() -> render((HomeState) event.getNewValue())));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                if (getRootPane() != null) {
                    getRootPane().setDefaultButton(null);
                }
                logOut.requestFocusInWindow();
            }
        });
    }

    private void render(HomeState state) {
        signedInAs.setText(state.signedInAs());
        handle.setText(state.handle());
    }
}
