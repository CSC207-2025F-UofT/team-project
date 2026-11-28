package entity;

final class TemplateText {
    private TemplateText() { }

    static String require(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " must contain text");
        return value;
    }
}
