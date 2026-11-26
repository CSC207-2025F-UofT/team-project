package org.example.cocktailcraft.view.language;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;

/** One immutable language context shared by all views in an application. */
public final class I18n {
    private static final String BUNDLE = "i18n.messages";
    private final Language language;
    private final ResourceBundle messages;
    private final ResourceBundle english;

    public I18n(Language language) {
        this.language = Objects.requireNonNull(language);
        ResourceBundle.Control control =
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        messages = ResourceBundle.getBundle(BUNDLE, language.getLocale(), control);
        english = ResourceBundle.getBundle(BUNDLE, Language.ENGLISH.getLocale(), control);
    }

    public Language getLanguage() {
        return language;
    }

    public String text(String key, Object... arguments) {
        String pattern = messages.getString(key);
        if (arguments.length == 0) {
            return pattern;
        }
        return new MessageFormat(pattern, language.getLocale()).format(arguments);
    }

    /** Translate known application messages, never user-entered recipe text. */
    public String message(String original) {
        if (original == null) {
            return "";
        }
        for (String key : english.keySet()) {
            if (key.startsWith("message.") && english.getString(key).equals(original)) {
                return text(key);
            }
        }
        String suffix = ": Cocktail does not exist.";
        if (original.endsWith(suffix)) {
            return original.substring(0, original.length() - suffix.length()) + ": " + text("cocktail.missing");
        }
        return original;
    }
}
