package data_access;

import entity.DialogueNode;
import entity.DialogueOption;
import entity.NPC;
import entity.Outcome;
import entity.StatEffects;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.talk_to_npc.DialogueDataAccessInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads the characters and their conversations from a JSON file in the
 * resources folder (dialogues.json).
 *
 * The whole file is read once, when this object is created. After that,
 * getNpc just looks the character up in a map. Because the conversations
 * live in a file, we can add or change them without touching any code.
 */
public class JsonDialogueDataAccessObject implements DialogueDataAccessInterface {

    private final Map<String, NPC> npcs = new HashMap<String, NPC>();

    /**
     * @param fileName the name of the JSON file in the resources folder,
     *                 e.g. "dialogues.json"
     */
    public JsonDialogueDataAccessObject(String fileName) {
        String fileText = readFile(fileName);
        JSONObject root = new JSONObject(fileText);

        JSONArray npcArray = root.getJSONArray("npcs");
        for (int i = 0; i < npcArray.length(); i++) {
            NPC npc = readNpc(npcArray.getJSONObject(i));
            npcs.put(npc.getId(), npc);
        }
    }

    @Override
    public NPC getNpc(String npcId) {
        return npcs.get(npcId);
    }

    /** Reads the whole file into one String. */
    private String readFile(String fileName) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (stream == null) {
            throw new RuntimeException("Could not find the dialogue file: " + fileName);
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8));
            StringBuilder text = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                text.append(line);
                text.append("\n");
                line = reader.readLine();
            }
            reader.close();
            return text.toString();
        } catch (IOException e) {
            // The game cannot run without its conversations, so stop here
            // with a message that says which file was the problem.
            throw new RuntimeException("Could not read the dialogue file: " + fileName, e);
        }
    }

    private NPC readNpc(JSONObject npcJson) {
        List<DialogueNode> nodes = new ArrayList<DialogueNode>();
        JSONArray nodeArray = npcJson.getJSONArray("nodes");
        for (int i = 0; i < nodeArray.length(); i++) {
            nodes.add(readNode(nodeArray.getJSONObject(i)));
        }

        return new NPC(
                npcJson.getString("id"),
                npcJson.getString("name"),
                npcJson.getString("startNodeId"),
                nodes);
    }

    private DialogueNode readNode(JSONObject nodeJson) {
        List<DialogueOption> options = new ArrayList<DialogueOption>();
        JSONArray optionArray = nodeJson.getJSONArray("options");
        for (int i = 0; i < optionArray.length(); i++) {
            options.add(readOption(optionArray.getJSONObject(i)));
        }

        return new DialogueNode(
                nodeJson.getString("id"),
                nodeJson.getString("text"),
                options);
    }

    private DialogueOption readOption(JSONObject optionJson) {
        // The four stat changes are optional in the file. If one is left
        // out, optInt gives us 0, which means "no change".
        StatEffects effects = new StatEffects(
                optionJson.optInt("energy", 0),
                optionJson.optInt("sanity", 0),
                optionJson.optInt("academic", 0),
                optionJson.optInt("money", 0));

        Outcome outcome = new Outcome.Builder()
                .effects(effects)
                .message(optionJson.optString("message", ""))
                .build();

        return new DialogueOption(
                optionJson.getString("id"),
                optionJson.getString("text"),
                outcome);
    }
}
