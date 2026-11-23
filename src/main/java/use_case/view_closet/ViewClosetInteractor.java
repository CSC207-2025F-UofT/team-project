package use_case.view_closet;

import java.util.ArrayList;
import java.util.List;
import entity.Category;
import entity.ClothingItem;

/**
 * TODO: RIE
 */
public class ViewClosetInteractor implements ViewClosetInputBoundary {
    private final ViewClosetDataAccessInterface wardrobe;
    private final ViewClosetOutputBoundary presenter;

    public ViewClosetInteractor(ViewClosetDataAccessInterface wardrobe,
                                ViewClosetOutputBoundary presenter) {
        this.wardrobe = wardrobe;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewClosetInputData inputData) {
        List<ClothingItem> allItems = wardrobe.getAllItems();
        List<ClothingItem> visibleItems = new ArrayList<>();

        for (ClothingItem item : allItems) {
            boolean matches;
            switch (inputData.getFilter()) {
                case ALL:
                    matches = true;
                    break;
                case TOPS:
                    matches = item.getCategory() == Category.TOP;
                    break;
                case BOTTOMS:
                    matches = item.getCategory() == Category.BOTTOM;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown closet filter");
            }
            if (matches) {
                visibleItems.add(item);
            }
        } // end of loop

        presenter.prepareSuccessView(new ViewClosetOutputData(visibleItems));
    }
}