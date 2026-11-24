package view;

import interface_adapter.view_history.ViewHistoryController;
import interface_adapter.view_history.ViewHistoryState;
import interface_adapter.view_history.ViewHistoryViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Shows the child's communication history, grouped by day.
 * Reloads the history every time the screen is opened.
 */
public class HistoryPanel extends JPanel implements PropertyChangeListener {

    private final ViewHistoryViewModel viewModel;
    private final JTextArea text = new JTextArea();

    public HistoryPanel(Navigator nav,
                        ViewHistoryController controller,
                        ViewHistoryViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        add(new HeaderBar(nav, ScreenNames.CAREGIVER_MENU, "Communication History"),
                BorderLayout.NORTH);

        text.setEditable(false);
        text.setFont(text.getFont().deriveFont(16f));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        wrapper.add(new JScrollPane(text), BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);

        // Load fresh history whenever the caregiver opens this screen.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                controller.execute();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ViewHistoryState state = viewModel.getState();

        if (state.getErrorMessage() != null) {
            text.setText(state.getErrorMessage());
            return;
        }

        StringBuilder builder = new StringBuilder();
        String currentDay = null;
        for (ViewHistoryState.HistoryRow row : state.getRows()) {
            if (!row.getDayLabel().equals(currentDay)) {
                if (currentDay != null) {
                    builder.append('\n');
                }
                currentDay = row.getDayLabel();
                builder.append(currentDay).append('\n');
            }
            builder.append("  ").append(row.getTimeLabel())
                    .append("   ").append(row.getText()).append('\n');
        }
        text.setText(builder.toString());
        text.setCaretPosition(0);
    }
}