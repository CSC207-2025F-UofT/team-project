package app;

import data_access.InMemoryOutfitRepository;
import data_access.LocalImageStore;
import entity.*;
import interface_adapter.outfit.*;
import interface_adapter.wardrobe.*;
import use_case.outfit.*;
import use_case.add_clothing.*;
import view.OutfitView;
import view.WardrobeView;
import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Demo wardrobe only. Replace repository with Ostap's implementation.
            // OutfitRepository and WardrobeRepository.
            InMemoryOutfitRepository repository = new InMemoryOutfitRepository(List.of(
                    new ClothingItem("hat-1", "Blue cap", ClothingType.HAT, "", List.of("blue")),
                    new ClothingItem("top-1", "White T-shirt", ClothingType.TOP, "", List.of("white")),
                    new ClothingItem("top-2", "Black sweater", ClothingType.TOP, "", List.of("black")),
                    new ClothingItem("bottom-1", "Blue jeans", ClothingType.BOTTOM, "", List.of("denim")),
                    new ClothingItem("shoes-1", "Sneakers", ClothingType.SHOES, "", List.of("white"))
            ));

            // Wardrobe / Add Clothing use case (UC1)
            WardrobeViewModel wardrobeModel = new WardrobeViewModel();
            WardrobePresenter wardrobePresenter = new WardrobePresenter(wardrobeModel);
            AddClothingInputBoundary addClothing = new AddClothingInteractor(
                    repository, new LocalImageStore(Path.of("wardrobe_images")), wardrobePresenter);
            WardrobeController wardrobeController = new WardrobeController(addClothing, wardrobeModel);

            // Outfit use case (UC3)
            OutfitViewModel outfitModel = new OutfitViewModel();
            OutfitPresenter outfitPresenter = new OutfitPresenter(outfitModel);
            OutfitInputBoundary outfitInteractor = new OutfitInteractor(repository, outfitPresenter);
            OutfitController outfitController = new OutfitController(outfitInteractor, outfitModel);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Wardrobe", new WardrobeView(wardrobeController, wardrobeModel));
            tabs.addTab("Outfits", new OutfitView(outfitController, outfitModel));
            // Outfits tab needs to see clothing that was just added.
            tabs.addChangeListener(e -> {
                if (tabs.getSelectedIndex() == 1) outfitController.load(null);
            });

            JFrame frame = new JFrame("Wear House - temporary storage");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(tabs);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            wardrobeController.load();
            outfitController.load(null);
        });
    }
}
