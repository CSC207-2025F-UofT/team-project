package use_case.ports;

public interface TranslationGateway {
    String translate(String text, String fromLang, String toLang);
}
