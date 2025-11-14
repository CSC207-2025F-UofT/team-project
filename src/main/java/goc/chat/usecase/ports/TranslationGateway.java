package goc.chat.usecase.ports;

public interface TranslationGateway {
    String translate(String text, String fromLang, String toLang);
}
