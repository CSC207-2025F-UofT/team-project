package app;

import data_access.InMemoryOutfitRepository;
import entity.*;
import interface_adapter.outfit.*;
import use_case.outfit.*;
import view.OutfitView;
import javax.swing.*;
import java.util.List;

public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Demo wardrobe only. Replace repository with teammate 3(aka Ostap)'s implementation.
            OutfitRepository repository = new InMemoryOutfitRepository(List.of(
                    new ClothingItem("hat-1", "Blue cap", ClothingType.HAT, "", List.of("blue")),
                    new ClothingItem("top-1", "White T-shirt", ClothingType.TOP, "", List.of("white")),
                    new ClothingItem("top-2", "Black sweater", ClothingType.TOP, "", List.of("black")),
                    new ClothingItem("bottom-1", "Blue jeans", ClothingType.BOTTOM, "", List.of("denim")),
                    new ClothingItem("shoes-1", "Sneakers", ClothingType.SHOES, "", List.of("white"))
            ));
            OutfitViewModel model = new OutfitViewModel();
            OutfitPresenter presenter = new OutfitPresenter(model);
            OutfitInputBoundary interactor = new OutfitInteractor(repository, presenter);
            OutfitController controller = new OutfitController(interactor, model);
            JFrame frame = new JFrame("Outfit Demo - temporary storage");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new OutfitView(controller, model));
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            controller.load(null);
        });
    }
}
