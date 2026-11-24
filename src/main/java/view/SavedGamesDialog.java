package view;

import entity.GameState;
import interface_adapter.persistence.PersistenceController;
import interface_adapter.persistence.PersistenceViewModel;
import use_case.SavedGameSummary;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class SavedGamesDialog {
    private static final String EMPTY = "No saved games yet. Start a new game and save your progress.";
    private static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("MMM d, uuuu  HH:mm:ss").withZone(ZoneId.systemDefault());

    private final Component owner;
    private final PersistenceController controller;
    private final PersistenceViewModel feedback;
    private final DefaultTableModel rows = new DefaultTableModel(new String[]{"Save name", "Day", "Last saved"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(rows);
    private final JLabel preview = new JLabel();
    private final JLabel status = new JLabel(" ");
    private final JButton load = new JButton("Load");
    private final JButton delete = new JButton("Delete");
    private final JButton close = new JButton("Close");

    private JDialog dialog;
    private List<SavedGameSummary> saves = List.of();

    public SavedGamesDialog(Component owner, PersistenceController controller, PersistenceViewModel feedback) {
        this.owner = owner;
        this.controller = controller;
        this.feedback = feedback;
        table.getSelectionModel().addListSelectionListener(event -> refreshControls());
        load.addActionListener(event -> {
            SavedGameSummary selected = selected();
            if (selected != null) {
                controller.load(selected.runId());
            }
        });
        delete.addActionListener(event -> confirmDeletion());
        close.addActionListener(event -> close());
        feedback.addPropertyChangeListener(event -> {
            switch (event.getPropertyName()) {
                case PersistenceViewModel.REQUEST_NAME -> requestName();
                case PersistenceViewModel.SAVES_UPDATED -> showSaves();
                case PersistenceViewModel.GAME_LOADED -> close();
                case "state" -> refreshControls();
                default -> { }
            }
        });
    }

    private void requestName() {
        String name = JOptionPane.showInputDialog(owner, "Save name (optional, up to 40 characters):",
                "Save Game", JOptionPane.PLAIN_MESSAGE);
        if (name != null) {
            controller.save(name);
        }
    }

    private void showSaves() {
        if (dialog == null) {
            createDialog();
            refreshList();
            dialog.setVisible(true);
        } else {
            refreshList();
        }
    }

    private void createDialog() {
        dialog = new JDialog(SwingUtilities.getWindowAncestor(owner), "Saved Games",
                JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (!feedback.getState().busy()) {
                    close();
                }
            }
        });

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16, 18, 14, 18));
        JLabel heading = new JLabel("Saved Games");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 20f));
        heading.setForeground(new Color(35, 62, 92));
        content.add(heading, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(0).setPreferredWidth(225);
        table.getColumnModel().getColumn(1).setPreferredWidth(65);
        table.getColumnModel().getColumn(2).setPreferredWidth(190);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.putClientProperty("html.disable", true);
        table.setDefaultRenderer(Object.class, renderer);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(510, 190));
        content.add(scroll, BorderLayout.CENTER);

        JPanel details = new JPanel(new BorderLayout(0, 8));
        details.add(preview, BorderLayout.NORTH);
        details.add(status, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.add(delete);
        actions.add(close);
        actions.add(load);
        details.add(actions, BorderLayout.SOUTH);
        content.add(details, BorderLayout.SOUTH);
        dialog.setContentPane(content);
        dialog.getRootPane().setDefaultButton(load);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
    }

    private void refreshList() {
        table.clearSelection();
        rows.setRowCount(0);
        saves = feedback.getSaves();
        for (SavedGameSummary save : saves) {
            String timestamp = save.savedAt() == null ? "Unknown (older save)" : DATE_TIME.format(save.savedAt());
            rows.addRow(new Object[]{save.name(), save.day() + " / " + GameState.TOTAL_DAYS, timestamp});
        }
        refreshControls();
    }

    private SavedGameSummary selected() {
        int index = table.getSelectedRow();
        return index >= 0 && index < saves.size() ? saves.get(index) : null;
    }

    private void refreshControls() {
        if (dialog == null) {
            return;
        }
        SavedGameSummary selected = selected();
        boolean busy = feedback.getState().busy();
        table.setEnabled(!busy);
        load.setEnabled(!busy && selected != null);
        delete.setEnabled(!busy && selected != null);
        close.setEnabled(!busy);
        preview.setText(selected == null ? (saves.isEmpty() ? EMPTY : "Select a saved game to preview its stats.")
                : "Energy  " + selected.energy() + "     Sanity  " + selected.sanity()
                + "     Academic Survival  " + selected.academicSurvival() + "     Money  $" + selected.money());
        String message = feedback.getState().message();
        status.setText(message.isEmpty() ? " " : message);
    }

    private void confirmDeletion() {
        SavedGameSummary selected = selected();
        if (selected != null && JOptionPane.showConfirmDialog(dialog,
                "Delete '" + selected.name() + "'? This action cannot be undone.", "Delete Saved Game",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            controller.delete(selected.runId());
        }
    }

    private void close() {
        if (dialog != null) {
            dialog.dispose();
            dialog = null;
        }
    }
}
