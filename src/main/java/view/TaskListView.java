package view;

import interface_adapter.edit_task.EditTaskController;
import interface_adapter.edit_task.EditTaskViewModel;
import use_case.edit_task.EditTaskOutputData;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This is a emporary task list for exercising the Edit Task use case while the main view is in development.
 *
 * <p>The main view can reuse the selection listener and the Edit Task button handler below.
 * Enable editing when the user selects a task, then pass that task and the signed-in user's ID
 * to {@code EditTaskView.open(...)}. A successful save returns the updated task so the main
 * view can refresh its list. Cancelling returns an empty result and preserves the original row.
 *
 * <p>Once the main view provides this flow, remove this panel and its sample-list wiring
 * from {@code AppBuilder}.
 */
public class TaskListView extends JPanel {
    public TaskListView(int currentUserId, List<EditTaskOutputData> tasks,
                        EditTaskController controller, EditTaskViewModel viewModel) {
        super(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        DefaultListModel<EditTaskOutputData> model = new DefaultListModel<>();
        tasks.stream().filter(task -> task.getUniqueUserId() == currentUserId).forEach(model::addElement);
        JList<EditTaskOutputData> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(6);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.putClientProperty("html.disable", true);
        list.setCellRenderer((source, task, index, selected, focus) -> {
            String title = task.getTitle().isEmpty() ? "(Untitled)" : task.getTitle();
            String text = String.format("%s   ·   %s   ·   %d%%", title, task.getDeadline(), task.getCompletionProgress());
            return renderer.getListCellRendererComponent(source, text, index, selected, focus);
        });
        JLabel heading = new JLabel("My tasks");
        heading.setLabelFor(list);
        add(heading, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton edit = new JButton("Edit Task");
        edit.setEnabled(false);
        list.addListSelectionListener(event -> edit.setEnabled(!list.isSelectionEmpty()));
        edit.addActionListener(event -> {
            int index = list.getSelectedIndex();
            if (index >= 0) {
                EditTaskView.open(this, currentUserId, model.get(index), controller, viewModel)
                        .ifPresent(saved -> model.set(index, saved));
            }
        });
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        actions.add(edit);
        add(actions, BorderLayout.SOUTH);
    }
}
