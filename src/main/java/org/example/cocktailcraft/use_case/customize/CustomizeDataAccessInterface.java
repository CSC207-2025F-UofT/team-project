package org.example.cocktailcraft.use_case.customize;

import java.io.IOException;
import java.util.Optional;
import org.example.cocktailcraft.entity.CustomRecipe;

public interface CustomizeDataAccessInterface {
    Optional<CustomRecipe> findById(String customRecipeId)
            throws IOException;

    void save(CustomRecipe recipe) throws IOException;
}

