package data_access;

import com.google.gson.*;
import entities.Deck;

import java.io.File;

import entities.Card;
import entities.Deck;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FlashcardDAO implements FlashcardDataAccessInterface {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path folder = Path.of("flashcard_decks");

    /**
     * Saves Deck into a new json file named based off how many Decks have been created already.
     * @param deck Entity Deck that is being saved
     */
    @Override
    public void saveDeck(Deck deck) {
        try {
            Files.createDirectories(folder);
            String title = "Flashcard Deck" + nextDeckNumber();

            JsonObject deckJson = new JsonObject();
            deckJson.addProperty("title", title);
            deck.setTitle(title);
            JsonArray cardsJson = new JsonArray();

            for(Card card : deck.getFlashcardDeck() ){
                // Add each card to the deck
                JsonObject cardJson = new JsonObject();
                cardJson.addProperty("question", card.getQuestion());
                cardJson.addProperty("answer", card.getAnswer());

                cardsJson.add(cardJson);
            }
            deckJson.add("cards", cardsJson);

            Path file = folder.resolve(title + ".json");
            try(Writer writer = Files.newBufferedWriter(file)) {
                gson.toJson(deckJson, writer); // writes everything to the file
            }

        } catch (IOException e) {
            throw new IllegalStateException("Failed to save deck", e);
        }

    }

    /**
     * Loads Deck mapped to deckId's JSON file and creates a Deck object.
     * @param deckId the flashcard deck number
     * @return the Entity Deck mapped to deckId
     */
    @Override
    public Deck loadDeck(String deckId) throws IllegalStateException {
        Path file = folder.resolve(deckId + ".json");

        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject deckJson = JsonParser.parseReader(reader).getAsJsonObject();

            String title = deckJson.get("title").getAsString();

            JsonArray cardsJson = deckJson.getAsJsonArray("cards");

            ArrayList<Card> cards = new ArrayList<>();

            for (int i =0; i < cardsJson.size(); i++){
                JsonObject jsonCard = cardsJson.get(i).getAsJsonObject();
                String question = jsonCard.get("question").getAsString();
                String answer = jsonCard.get("answer").getAsString();
                Card card = new Card(question,answer);
                cards.add(card);

            }
            Deck deck = new Deck(title, cards);
            return deck;

        } catch (IOException e) {
            throw new IllegalStateException("Could not read file", e);
        }
    }

    /**
     * Checks existing JSON files and returns the first available deck number
     * @return the next available deck number
     */
    private int nextDeckNumber() {
        int number = 1;

        while (Files.exists(folder.resolve("Flashcard Deck" + number + ".json"))) {
            number++;
        }
        return number;

    }

}

