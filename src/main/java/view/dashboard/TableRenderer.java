package view.dashboard;

import interface_adapter.dashboard.DashboardItemState;
import interface_adapter.dashboard.DashboardStateCatalog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Render analytics table given a table state.
 */
public class TableRenderer implements DashboardRenderer<DashboardStateCatalog.TableState> {
    private final float TITLE_SIZE = 15f;
    @Override
    public JComponent render(DashboardStateCatalog.TableState tableState) {
        String[] columns = tableState.columns().toArray(new String[0]);
        Object[][] data = tableState.rows().stream()
                .map(List::toArray)
                .toArray(Object[][]::new);

        // Create a model and override isCellEditable to make it read-only
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(tableState.title());
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_SIZE));
        tablePanel.add(title);
        JTable table = new JTable(model);
        tablePanel.add(new JScrollPane(table));
        return tablePanel;
    }
}
