package entity;

import java.util.ArrayList;

public class EmptyPokemonFactory {
    public Pokemon create(){
        return new Pokemon("", new Type("", 0, new ArrayList<>(), new ArrayList<>()),
                new Type("", 0, new ArrayList<>(), new ArrayList<>()),
                new ArrayList<Integer>(), new ArrayList<Integer>(),
                0, new ArrayList<Integer>(), new ArrayList<Integer>(),
                new ArrayList<Integer>());
    }
}
