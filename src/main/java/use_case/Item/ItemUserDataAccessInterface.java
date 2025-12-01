package use_case.Item;
import entity.Item;

public interface ItemUserDataAccessInterface {

    /**
     * @param name name of the item to be fetched
     * @return item
     *
     */
    Item getItemByName(String name);

}
