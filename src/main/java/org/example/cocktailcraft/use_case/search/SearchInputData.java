package org.example.cocktailcraft.use_case.search;

import java.util.List;

/** Initial input shape. TODO(Qinli): extend with the agreed use case contract. */
public class SearchInputData {
    private final String name;
    private final List<String> baseSpirits;

    public SearchInputData(String name, List<String> baseSpirits) {
        this.name = name;
        this.baseSpirits = List.copyOf(baseSpirits);
    }

    public String getName() {
        return name;
    }

    public List<String> getBaseSpirits() {
        return baseSpirits;
    }
}
