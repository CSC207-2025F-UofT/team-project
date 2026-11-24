package data_access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.google_login.GoogleIdentityGateway;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/** Google authorization-code flow for a desktop OAuth client. */
public final class GoogleDesktopIdentityGateway implements GoogleIdentityGateway {
    private static final String CALLBACK_PATH = "/oauth/callback";
    private static final String LOOPBACK = "127.0.0.1";
    private static final Duration HTTP_TIMEOUT = Duration.ofSeconds(20);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final String clientId;
    private final String clientSecret;
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(HTTP_TIMEOUT).build();

    public GoogleDesktopIdentityGateway(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String authenticate() throws IOException, InterruptedException {
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            throw new IOException("Google sign-in needs a desktop web browser.");
        }

        String state = randomToken();
        String verifier = randomToken();
        HttpServer server = HttpServer.create(new InetSocketAddress(LOOPBACK, 0), 0);
        String redirectUri = "http://" + LOOPBACK + ":" + server.getAddress().getPort() + CALLBACK_PATH;
        CompletableFuture<String> callback = new CompletableFuture<>();
        String code;

        try {
            server.createContext(CALLBACK_PATH, exchange -> handleCallback(exchange, state, callback));
            server.start();
            String query = form(Map.of(
                    "client_id", clientId,
                    "redirect_uri", redirectUri,
                    "response_type", "code",
                    "scope", "openid profile",
                    "state", state,
                    "code_challenge", challenge(verifier),
                    "code_challenge_method", "S256",
                    "prompt", "select_account"));
            Desktop.getDesktop().browse(URI.create("https://accounts.google.com/o/oauth2/v2/auth?" + query));
            code = callback.get(2, TimeUnit.MINUTES);
        }
        catch (TimeoutException exception) {
            throw new IOException("Google sign-in timed out. Try again.");
        }
        catch (ExecutionException exception) {
            throw new IOException("Google sign-in was declined or cancelled.");
        }
        catch (IOException exception) {
            throw new IOException("Could not open the browser for Google sign-in.", exception);
        }
        finally {
            server.stop(0);
        }

        HttpRequest tokenRequest = HttpRequest.newBuilder(URI.create("https://oauth2.googleapis.com/token"))
                .timeout(HTTP_TIMEOUT)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form(Map.of(
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "code", code,
                        "code_verifier", verifier,
                        "grant_type", "authorization_code",
                        "redirect_uri", redirectUri))))
                .build();
        String accessToken = readGoogleField(tokenRequest, "access_token", "exchange the sign-in code");

        HttpRequest profileRequest = HttpRequest.newBuilder(
                        URI.create("https://openidconnect.googleapis.com/v1/userinfo"))
                .timeout(HTTP_TIMEOUT)
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        return readGoogleField(profileRequest, "sub", "read the account ID");
    }

    private String readGoogleField(HttpRequest request, String field, String operation)
            throws IOException, InterruptedException {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Google returned HTTP " + response.statusCode() + ".");
            }
            String value = new JSONObject(response.body()).getString(field);
            if (value.isBlank()) {
                throw new IOException("Google returned an empty field.");
            }
            return value;
        }
        catch (IOException | JSONException exception) {
            throw new IOException("Could not " + operation + " with Google. Try again.", exception);
        }
    }

    private static void handleCallback(HttpExchange exchange, String state, CompletableFuture<String> result)
            throws IOException {
        try {
            if (!"GET".equals(exchange.getRequestMethod())
                    || !CALLBACK_PATH.equals(exchange.getRequestURI().getPath())) {
                respond(exchange, 404, "Page not found.");
                return;
            }
            Map<String, String> parameters;
            try {
                parameters = parseQuery(exchange.getRequestURI().getRawQuery());
            }
            catch (IllegalArgumentException exception) {
                respond(exchange, 400, "Invalid sign-in response.");
                return;
            }
            if (!state.equals(parameters.get("state"))) {
                respond(exchange, 400, "Invalid sign-in response.");
                return;
            }
            String code = parameters.get("code");
            if (parameters.containsKey("error") || code == null || code.isBlank()) {
                try {
                    respond(exchange, 200, "Google sign-in was cancelled. Return to the application.");
                }
                finally {
                    result.completeExceptionally(new IOException("Google authorization was declined."));
                }
            }
            else {
                try {
                    respond(exchange, 200, "Return to the application to finish signing in.");
                }
                finally {
                    result.complete(code);
                }
            }
        }
        finally {
            exchange.close();
        }
    }

    private static void respond(HttpExchange exchange, int status, String message) throws IOException {
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.getResponseHeaders().set("Referrer-Policy", "no-referrer");
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(body);
        }
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> parameters = new HashMap<>();
        if (query != null) {
            for (String pair : query.split("&")) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2) {
                    String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                    if (parameters.putIfAbsent(key, value) != null) {
                        throw new IllegalArgumentException("Repeated callback parameter.");
                    }
                }
            }
        }
        return parameters;
    }

    private static String form(Map<String, String> values) {
        return values.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String randomToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String challenge(String verifier) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256")
                    .digest(verifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        }
        catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable.", exception);
        }
    }
}
