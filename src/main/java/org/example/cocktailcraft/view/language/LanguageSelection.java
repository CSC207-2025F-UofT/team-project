package org.example.cocktailcraft.view.language;

import java.awt.Component;
import javax.swing.JOptionPane;

/** Startup choice; closing the dialog cancels application startup. */
public final class LanguageSelection {
    private LanguageSelection() {
    }

    public static Language choose(Component parent) {
        return (Language) JOptionPane.showInputDialog(
                parent,
                "Choose your language / 请选择语言 / Choisissez votre langue",
                "CocktailCraft — Language / 语言 / Langue",
                JOptionPane.QUESTION_MESSAGE,
                null,
                Language.values(),
                Language.ENGLISH
        );
    }
}
