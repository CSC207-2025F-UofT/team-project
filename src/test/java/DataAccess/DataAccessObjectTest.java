package DataAccess;
import data_access.DataAccessObject;
import entity.Pokemon;
import entity.Team;
import org.junit.Test;
import java.util.ArrayList;


public class DataAccessObjectTest {

    @Test
    public void AccessTest() {
        DataAccessObject d = new DataAccessObject();
        Team t = new Team("Ash's Team");

        int type1 = 1;
        int type2 = 0;

        ArrayList<Integer> stats = new ArrayList<>();
        stats.add(1);
        stats.add(2);
        stats.add(3);
        stats.add(4);
        stats.add(5);
        stats.add(6);

        ArrayList<Integer> abilities = new ArrayList<>();
        abilities.add(1);
        abilities.add(2);

        int hidden = 0;

        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(0);

        ArrayList<Integer> egggroup = new ArrayList<>();
        egggroup.add(1);

        ArrayList<Integer> pokedexes = new ArrayList<>();
        egggroup.add(1);


        Pokemon pikachu = new Pokemon("pikachu", type1, type2, stats, abilities, hidden,  moves, egggroup, pokedexes);
        Pokemon charmander = new Pokemon("charmander", type1, type2, stats, abilities, hidden,  moves, egggroup, pokedexes);
        Pokemon squirtle = new Pokemon("squirtle", type1, type2, stats, abilities, hidden,  moves, egggroup, pokedexes);
        Pokemon bulbasaur = new Pokemon("bulbasaur", type1, type2, stats, abilities, hidden,  moves, egggroup, pokedexes);
        Pokemon mewtwo = new Pokemon("mewtwo", type1, type2, stats, abilities, hidden,  moves, egggroup, pokedexes);
        Pokemon mew = new Pokemon("mew", type1, type2, stats, abilities, hidden,  moves, egggroup, pokedexes);

        t.setPokemon(pikachu, 0);
        t.setPokemon(charmander, 1);
        t.setPokemon(squirtle, 2);
        t.setPokemon(bulbasaur, 3);
        t.setPokemon(mewtwo, 4);
        t.setPokemon(mew, 5);



        d.saveTeam(t);

    }
}
