package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Home's search component. TODO(Qinli): add base-spirit selection and results. */
public class SearchView extends JPanel {
    public SearchView(SearchController controller, SearchViewModel model) {
        super(new BorderLayout());
        JTextField name = new JTextField(22);
        JPanel controls = new JPanel();
        controls.add(new JLabel("Cocktail name:"));
        controls.add(name);
        JButton spirits = new JButton("Base spirits (TODO)");
        spirits.setEnabled(false);
        controls.add(spirits);
        controls.add(ViewSupport.button("Search", () -> controller.execute(name.getText(), List.of())));
        controls.add(ViewSupport.button("Clear input", () -> name.setText("")));
        add(controls, BorderLayout.CENTER);
        add(ViewSupport.status(model), BorderLayout.SOUTH);
    }
}
