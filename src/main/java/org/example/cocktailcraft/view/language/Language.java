package org.example.cocktailcraft.view.language;

import java.util.Locale;

/** Supported interface languages, displayed in their own language. */
public enum Language {
    ENGLISH("English", Locale.ENGLISH),
    CHINESE("简体中文", Locale.SIMPLIFIED_CHINESE),
    FRENCH("Français", Locale.FRENCH);

    private final String displayName;
    private final Locale locale;

    Language(String displayName, Locale locale) {
        this.displayName = displayName;
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
