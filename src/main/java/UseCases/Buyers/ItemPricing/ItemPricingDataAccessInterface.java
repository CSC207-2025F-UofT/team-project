package UseCases.Buyers.ItemPricing;

import Entities.Item;

import java.io.IOException;
import java.util.ArrayList;

public interface ItemPricingDataAccessInterface {

    ArrayList<Item> getItemsForSearchTerm(String searchTerm) throws IOException;

}
