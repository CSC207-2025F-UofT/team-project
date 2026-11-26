package org.example.cocktailcraft.data_access;

import org.example.cocktailcraft.entity.Cocktail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeTranslationsTest {
    @Test
    void translatesTheCatalogRecipeWithoutChangingItsIdentityOrQuantities() throws Exception {
        Cocktail source = new CocktailJsonReader().read("/bar-assistant/data/cocktails/andalusian-buck/data.json");
        Cocktail chinese = new RecipeTranslations("zh").translate(source);
        Cocktail french = new RecipeTranslations("fr").translate(source);

        assertEquals(source.getId(), chinese.getId());
        assertEquals(source.getIngredients().get(0).getAmount(), chinese.getIngredients().get(0).getAmount());
        assertEquals("安达卢西亚巴克", chinese.getName());
        assertEquals("金酒", chinese.getIngredients().get(0).getName());
        assertEquals("毫升", chinese.getIngredients().get(0).getUnit());
        assertTrue(chinese.getInstructions().contains("摇匀至充分冷却"));
        assertEquals("Buck andalou", french.getName());
        assertTrue(french.getInstructions().contains("Secouez"));
        assertEquals("Andalusian Buck", source.getName());
        assertEquals(source, new RecipeTranslations("en").translate(source));
    }
}
