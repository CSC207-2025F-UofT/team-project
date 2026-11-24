package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import entities.CognitiveData;
import interface_adapter.Loggedin.LoggedinState;
import interface_adapter.Loggedin.LoggedinViewModel;
import interface_adapter.Loggedin.LoggedinViewModelListener;
import interface_adapter.Loggedin.get_dataController;

/**
 * The View shown once the user has logged in. It lets the user load their saved data.
 */
public class LoggedinView extends JPanel implements ActionListener, LoggedinViewModelListener {

    private final LoggedinViewModel loggedinViewModel;

    private final JLabel greetingField = new JLabel();
    private final JLabel dataErrorField = new JLabel();
    private final JTextArea dataArea = new JTextArea(10, 40);
    private final JButton loadData;
    private get_dataController getDataController = null;

    public LoggedinView(LoggedinViewModel loggedinViewModel) {
        this.loggedinViewModel = loggedinViewModel;
        this.loggedinViewModel.addListener(this);

        final JLabel title = new JLabel("Logged In");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        greetingField.setAlignmentX(Component.CENTER_ALIGNMENT);
        dataErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        dataErrorField.setForeground(Color.RED);

        dataArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(dataArea);
        scrollPane.setPreferredSize(new Dimension(450, 200));

        final JPanel buttons = new JPanel();
        loadData = new JButton("Load my data");
        buttons.add(loadData);
        loadData.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(greetingField);
        this.add(buttons);
        this.add(dataErrorField);
        this.add(scrollPane);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(loadData)) {
            getDataController.execute(loggedinViewModel.getState().getUsername());
        }
    }

    @Override
    public void stateChanged() {
        final LoggedinState state = loggedinViewModel.getState();
        greetingField.setText(state.getUsername().isEmpty()
                ? "" : "Hello, " + state.getUsername() + " (" + state.getRole() + ")");
        dataErrorField.setText(state.getDataError());
        dataArea.setText(formatEntries(state));
    }

    private static String formatEntries(LoggedinState state) {
        if (!state.isDataLoaded()) {
            return "";
        }
        final List<CognitiveData> entries = state.getEntries();
        if (entries.isEmpty()) {
            return "No data saved yet.";
        }
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final StringBuilder sb = new StringBuilder();
        for (CognitiveData d : entries) {
            sb.append(format.format(d.getDate()))
                    .append("  mood: ").append(d.getMood())
                    .append("  ").append(d.getCogTestType()).append(" test: ").append(d.getCogTestResult())
                    .append("  productivity: ").append(d.getProductivityRating())
                    .append('\n');
        }
        return sb.toString();
    }

    public void setGetDataController(get_dataController getDataController) {
        this.getDataController = getDataController;
    }
}
