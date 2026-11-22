package use_case.add_clothing;


import entity.ClothingItem;

/**
 * The storage methods AddClothingInteractor needs.
 */
public interface AddClothingDataAccessInterface {
    /**
     * Who is logged in right now
     * @return the current username
     */
    String getCurrentUsername();

    /**
     * Makes a new id that isn't used yet in this user's closet
     * @param username whose closet
     * @return e.g. "c001"
     */
    String generateItemId(String username);

    /**
     * Adds the item to this user's closet and saves it
     * @param username whose closet
     * @param item     the new item
     */
    void saveItem(String username, ClothingItem item);
}
