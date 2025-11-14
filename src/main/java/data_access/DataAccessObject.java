package data_access;
import java.io.*;
import java.util.ArrayList;

import entity.Pokemon;
import entity.Team;
import use_case.BuildPokemonTeam.BuildPokemonTeamDataAccessInterface;

public class DataAccessObject implements BuildPokemonTeamDataAccessInterface {

    public void saveTeam(Team team) {
        String teamName = team.getTeamName();
        Pokemon p1 = team.getPokemon(0);
        Pokemon p2 = team.getPokemon(1);
        Pokemon p3 = team.getPokemon(2);
        Pokemon p4 = team.getPokemon(3);
        Pokemon p5 = team.getPokemon(4);
        Pokemon p6 = team.getPokemon(5);
        ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
        pokemons.add(p1);
        pokemons.add(p2);
        pokemons.add(p3);
        pokemons.add(p4);
        pokemons.add(p5);
        pokemons.add(p6);

        try (FileReader x = new FileReader("teamStorage/teams.csv")) {
            System.out.println("SUCCESS");
            System.out.println(teamName);
            System.out.println(pokemons + "\n");


            FileWriter writer = new FileWriter("teamStorage/teams.csv", true);

            writer.write(team.getTeamName() + "," + " ");

            for (int i = 0; i < pokemons.size(); i++) {
                if (i > 0) writer.write("," + " "); {
                    writer.write(team.getPokemon(i).getName());
                }
            }


            writer.write("\n");
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Team loadTeam(String name){
        Team team = null;
        return team;
    }
    public boolean exists(String name){
        boolean x = true;
        return x;
    }

}

