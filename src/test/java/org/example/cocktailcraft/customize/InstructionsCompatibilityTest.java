package org.example.cocktailcraft.customize;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.CustomRecipe;
import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeController;
import org.example.cocktailcraft.use_case.click.ClickOutputData;
import org.example.cocktailcraft.use_case.customize.CustomizeInputData;
import org.example.cocktailcraft.use_case.customize.CustomizeOutputData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InstructionsCompatibilityTest {
    @Test
    void legacyConstructorsStillWorkWithEmptyInstructions() {
        List<RecipeIngredient> ingredients = List.of(new RecipeIngredient("Sugar", "1", "tsp"));
        assertEquals("", new Cocktail("mojito", "Mojito", ingredients, new Tags(Set.of())).getInstructions());
        assertEquals("", new CustomRecipe("custom-001", "user-001", "mojito", "My Mojito",
                ingredients).getInstructions());
        assertEquals("", new CustomizeInputData(null, "user-001", "mojito", "My Mojito",
                ingredients).getInstructions());
        assertEquals("", new CustomizeOutputData("custom-001", "My Mojito", ingredients).getInstructions());
        assertEquals("", new ClickOutputData(List.of("Sugar: 1 tsp")).getInstructions());
    }

    @Test
    void controllerForwardsInstructionsAndRetainsLegacyExecute() {
        AtomicReference<CustomizeInputData> received = new AtomicReference<>();
        CustomizeController controller = new CustomizeController(received::set);
        List<RecipeIngredient> ingredients = List.of(new RecipeIngredient("Sugar", "1", "tsp"));
        controller.execute(null, "user-001", "mojito", "My Mojito", ingredients, "Stir.\nThen serve.");
        assertNotNull(received.get());
        assertEquals("Stir.\nThen serve.", received.get().getInstructions());
        controller.execute(null, "user-001", "mojito", "My Mojito", ingredients);
        assertEquals("", received.get().getInstructions());
    }
}
