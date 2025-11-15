package view;

import entity.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayPokemonJPanel extends JPanel {

    JLabel spriteLabel = new JLabel();
    JPanel pokemonInfo = new JPanel();

    DisplayPokemonJPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(spriteLabel);
        this.add(pokemonInfo);
    }
    public void setPokemon(Pokemon pokemon) {
        try {
            URL spriteURL = new URL(pokemon.getSprite());
            Image sprite = (new ImageIcon(spriteURL).getImage()).getScaledInstance(480, 480, Image.SCALE_DEFAULT);

            spriteLabel.setIcon(new ImageIcon(sprite));

            JPanel pokemonInfo = new JPanel();
            pokemonInfo.add(new JLabel("Name: " + pokemon.getName()));
            pokemonInfo.add(new JLabel("Type 1: " + pokemon.getType1()));
            if (pokemon.getType2() != null) {
                pokemonInfo.add(new JLabel("Type 2: " + pokemon.getType2()));
            }
            pokemonInfo.setLayout(new BoxLayout(pokemonInfo, BoxLayout.Y_AXIS));

            this.pokemonInfo.removeAll();
            this.pokemonInfo.add(pokemonInfo);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
