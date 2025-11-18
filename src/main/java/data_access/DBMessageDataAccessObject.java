package data_access;

import entity.Message;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.ports.MessageRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBMessageDataAccessObject implements MessageRepository {

    private static final int SUCCESS_CODE = 200;
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String MESSAGE_LABEL = "message";

    private static final String BASE_URL = "http://vm003.teach.cs.toronto.edu:20112";
    private static final String PATH_MESSAGE = "/message";
    private static final String PATH_MESSAGES_BY_CHAT = "/messages";

    @Override
    public Optional<Message> findById(String id) {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();

        final String url = String.format("%s%s?id=%s", BASE_URL, PATH_MESSAGE, id);

        final Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try (Response response = client.newCall(request).execute()) {

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) != SUCCESS_CODE) {
                return Optional.empty();
            }

            final JSONObject msgJson = responseBody.getJSONObject("message");
            return Optional.of(jsonToMessage(msgJson));
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Message save(Message message) {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();

        final JSONObject requestBody = new JSONObject();
        requestBody.put("id", message.getId());
        requestBody.put("chat_id", message.getChatId());
        requestBody.put("sender_user_id", message.getSenderUserId());
        requestBody.put("content", message.getContent());
        requestBody.put("timestamp", message.getTimestamp().toString());

        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);

        final String url = BASE_URL + PATH_MESSAGE;

        final Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try (Response response = client.newCall(request).execute()) {

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) != SUCCESS_CODE) {
                throw new RuntimeException(responseBody.getString(MESSAGE_LABEL));
            }

            final JSONObject msgJson = responseBody.getJSONObject("message");
            return jsonToMessage(msgJson);
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Message> findByChatId(String chatId) {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();

        final String url = String.format("%s%s?chatId=%s", BASE_URL, PATH_MESSAGES_BY_CHAT, chatId);

        final Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try (Response response = client.newCall(request).execute()) {

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) != SUCCESS_CODE) {
                return new ArrayList<>();
            }

            final JSONArray array = responseBody.getJSONArray("messages");
            final List<Message> result = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                final JSONObject msgJson = array.getJSONObject(i);
                result.add(jsonToMessage(msgJson));
            }

            return result;
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteById(String id) {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();

        final String url = String.format("%s%s?id=%s", BASE_URL, PATH_MESSAGE, id);

        final Request request = new Request.Builder()
                .url(url)
                .method("DELETE", null)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try (Response response = client.newCall(request).execute()) {

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) != SUCCESS_CODE) {
                throw new RuntimeException(responseBody.getString(MESSAGE_LABEL));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Message jsonToMessage(JSONObject json) {
        final String id = json.getString("id");
        final String chatId = json.getString("chat_id");
        final String senderUserId = json.getString("sender_user_id");
        final String content = json.getString("content");
        final Instant timestamp = Instant.parse(json.getString("timestamp"));

        return new Message(id, chatId, senderUserId, content, timestamp);
    }
}
