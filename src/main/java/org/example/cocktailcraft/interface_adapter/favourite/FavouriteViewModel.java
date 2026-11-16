package org.example.cocktailcraft.interface_adapter.favourite;

import org.example.cocktailcraft.interface_adapter.ViewModel;

public class FavouriteViewModel extends ViewModel<FavouriteState> {
    public FavouriteViewModel() {
        super(new FavouriteState());
    }
}
