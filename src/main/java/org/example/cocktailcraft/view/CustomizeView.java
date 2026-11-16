package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.NavigationController;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeController;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeViewModel;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CustomizeView extends JPanel {
    public static final String VIEW_NAME = "customize";

    public CustomizeView(CustomizeController controller, CustomizeViewModel model,
                         NavigationController navigation) {
        super(new BorderLayout());
        JPanel content = ViewSupport.column();
        content.add(new JLabel("Customize - layout preview, no original recipe selected"));
        content.add(new JLabel("TODO(Enhao): recipe form, ingredients, amounts, Save, and Edit."));
        content.add(ViewSupport.button("Open recipe (scaffold)", () -> controller.execute("")));
        JButton save = new JButton("Save (not implemented)");
        save.setEnabled(false);
        content.add(save);
        content.add(ViewSupport.status(model));
        content.add(ViewSupport.button("Back to Details", () -> navigation.show(CocktailDetailsView.VIEW_NAME)));
        add(content, BorderLayout.CENTER);
    }
}
