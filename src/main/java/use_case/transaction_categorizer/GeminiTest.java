package use_case.transaction_categorizer;

public class GeminiTest {
    public static void main(String[] args) {
        GeminiClient client = new GeminiClient();
        System.out.println(client.askGemini("Say 'Hello World'"));
    }

}
