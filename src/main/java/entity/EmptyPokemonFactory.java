package entity;

import java.util.ArrayList;

public class EmptyPokemonFactory {
    public Pokemon create(){
        return new Pokemon("", 0, 0,
                new ArrayList<Integer>(), new ArrayList<Integer>(),
                0, new ArrayList<Integer>(), new ArrayList<Integer>(),
                new ArrayList<Integer>());
    }
}
