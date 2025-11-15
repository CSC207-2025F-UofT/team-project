package view;

import entity.Pokemon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DisplayPokemonJPanel extends JPanel {

    JLabel spriteLabel = new JLabel();
    JPanel pokemonInfo = new JPanel();

    DisplayPokemonJPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        pokemonInfo.setLayout(new BoxLayout(pokemonInfo, BoxLayout.Y_AXIS));
        this.add(spriteLabel);
        this.add(pokemonInfo);
    }
    public void setPokemon(Pokemon pokemon) {
        try {
            URL spriteURL = new URL(pokemon.getSprite());
            Image sprite = (new ImageIcon(spriteURL).getImage()).getScaledInstance(384, 384, Image.SCALE_DEFAULT);

            spriteLabel.setIcon(new ImageIcon(sprite));
            spriteLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JPanel pokemonBasicInfo = getPokemonBasicInfo(pokemon);
            JPanel pokemonStatsInfo = getPokemonStatsInfo(pokemon);

            this.pokemonInfo.removeAll();
            this.pokemonInfo.add(pokemonBasicInfo);
            this.pokemonInfo.add(pokemonStatsInfo);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static JPanel getPokemonBasicInfo(Pokemon pokemon) {
        JPanel basicPokemonInfo = new JPanel();
        basicPokemonInfo.setLayout(new BoxLayout(basicPokemonInfo, BoxLayout.Y_AXIS));
        basicPokemonInfo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        StringBuilder nameBuild = new StringBuilder();
        nameBuild.append(Character.toUpperCase(pokemon.getName().charAt(0)));
        nameBuild.append(pokemon.getName().substring(1));
        String name = nameBuild.toString();
        basicPokemonInfo.add(new JLabel("Name: " + name));
        basicPokemonInfo.add(new JLabel("Type 1: " + pokemon.getType1()));
        if (pokemon.getType2() != null) {
            basicPokemonInfo.add(new JLabel("Type 2: " + pokemon.getType2()));
        }
        return basicPokemonInfo;
    }

    @NotNull
    private static JPanel getPokemonStatsInfo(Pokemon pokemon) {
        JPanel pokemonStatsInfo = new JPanel();
        pokemonStatsInfo.setLayout(new BoxLayout(pokemonStatsInfo, BoxLayout.Y_AXIS));
        pokemonStatsInfo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        ArrayList stats = pokemon.getStats();
        pokemonStatsInfo.add(new JLabel("HP: " + stats.get(0)));
        pokemonStatsInfo.add(new JLabel("Attack: " + stats.get(1)));
        pokemonStatsInfo.add(new JLabel("Defense: " + stats.get(2)));
        pokemonStatsInfo.add(new JLabel("Special-Attack: " + stats.get(3)));
        pokemonStatsInfo.add(new JLabel("Special-Defense: " + stats.get(4)));
        pokemonStatsInfo.add(new JLabel("Speed: " + stats.get(5)));
        return pokemonStatsInfo;
    }
}
