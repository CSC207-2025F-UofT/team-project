package org.example.cocktailcraft.view.favourite;


import org.example.cocktailcraft.entity.User;
import org.example.cocktailcraft.interface_adapter.favourite.FavouriteController;
import org.example.cocktailcraft.interface_adapter.favourite.FavouriteState;
import org.example.cocktailcraft.interface_adapter.favourite.FavouriteViewModel;
import org.example.cocktailcraft.interface_adapter.favourite.FavouriteViewModelListener;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FavouriteView extends JPanel implements FavouriteViewModelListener {

    private final FavouriteViewModel favouriteViewModel;
    private final JButton favouriteButton;
    private final JLabel errorLabel;
    private FavouriteController favouriteController = null;
    private User user = null;
    private String cocktailID = null;

    public FavouriteView(FavouriteViewModel favouriteViewModel) {
        this.favouriteViewModel = favouriteViewModel;
        this.favouriteViewModel.addListener(this);

        setLayout(new FlowLayout());
        errorLabel = new JLabel();

        favouriteButton = new JButton();
        add(favouriteButton);

        favouriteButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(favouriteButton)) {
                            if (favouriteController != null && user != null && cocktailID != null) {
                                favouriteController.execute(user, cocktailID);
                            }
                        }
                    }
                }
        );



        favouriteButton.setEnabled(false);
        stateChanged();
    }

    public void stateChanged() {
        FavouriteState state = favouriteViewModel.getState();
        String error = state.getFavouriteError();
        if (error != null) {
            errorLabel.setText(error);
        } else {
            errorLabel.setText("");
            if (state.isFavourite()) {
                favouriteButton.setText("♥");
            } else {
                favouriteButton.setText("♡");
            }
        }
    }


    public void setFavouriteController(FavouriteController controller) {
        this.favouriteController = controller;
        setCurrentCocktail(user, cocktailID);
    }

    public void setUser(User user) {
        setCurrentCocktail(user, cocktailID);
    }

    public void setCurrentCocktail(User user, String cocktailID) {
        this.user = user;
        this.cocktailID = cocktailID;
        favouriteButton.setEnabled(favouriteController != null && user != null && cocktailID != null);
        favouriteButton.setText(user != null && cocktailID != null && user.isFavourite(cocktailID) ? "♥" : "♡");
    }
}