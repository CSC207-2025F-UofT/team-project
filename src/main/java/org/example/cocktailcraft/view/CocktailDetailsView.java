package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.NavigationController;
import org.example.cocktailcraft.interface_adapter.details.DetailsController;
import org.example.cocktailcraft.interface_adapter.details.DetailsViewModel;
import org.example.cocktailcraft.interface_adapter.favourite.FavouriteController;
import org.example.cocktailcraft.interface_adapter.favourite.FavouriteViewModel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CocktailDetailsView extends JPanel {
    public static final String VIEW_NAME = "details";

    public CocktailDetailsView(DetailsController details, DetailsViewModel detailsModel,
                               FavouriteController favourite, FavouriteViewModel favouriteModel,
                               NavigationController navigation) {
        super(new BorderLayout());
        JPanel content = ViewSupport.column();
        content.add(new JLabel("Cocktail Details - layout preview, no cocktail selected"));
        content.add(new JLabel("Image | Name | Ingredients | Quantities | Instructions | Description"));
        content.add(ViewSupport.status(detailsModel));
        content.add(ViewSupport.status(favouriteModel));
        // TODO(Wenqian): replace preview calls with the selected cocktail ID and full details flow.
        content.add(ViewSupport.button("Load details (scaffold)", () -> details.execute("")));
        content.add(ViewSupport.button("Favourite (scaffold)", () -> favourite.execute("")));
        content.add(ViewSupport.button("Preview Customize layout",
                () -> navigation.show(CustomizeView.VIEW_NAME)));
        content.add(ViewSupport.button("Home", () -> navigation.show(MainPageView.VIEW_NAME)));
        add(content, BorderLayout.CENTER);
    }
}
