package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.NavigationController;
import org.example.cocktailcraft.interface_adapter.browse.BrowseController;
import org.example.cocktailcraft.interface_adapter.browse.BrowseViewModel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPageView extends JPanel {
    public static final String VIEW_NAME = "home";

    public MainPageView(SearchView search, BrowseController controller,
                        BrowseViewModel model, NavigationController navigation) {
        super(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("CocktailCraft"), BorderLayout.WEST);
        header.add(search, BorderLayout.CENTER);
        header.add(ViewSupport.button("Profile", () -> navigation.show(ProfileView.VIEW_NAME)),
                BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        JPanel catalog = ViewSupport.column();
        catalog.add(new JLabel("Explore Cocktails"));
        catalog.add(new JLabel("Cocktail cards and images will appear here. Dataset: 663 recipes."));
        catalog.add(ViewSupport.button("Load catalog (scaffold)", controller::execute));
        catalog.add(ViewSupport.status(model));
        catalog.add(ViewSupport.button("Preview Details layout",
                () -> navigation.show(CocktailDetailsView.VIEW_NAME)));
        add(catalog, BorderLayout.CENTER);
        add(new JLabel("UI scaffold: no search, account, favourites, or saving is implemented."),
                BorderLayout.SOUTH);
    }
}
